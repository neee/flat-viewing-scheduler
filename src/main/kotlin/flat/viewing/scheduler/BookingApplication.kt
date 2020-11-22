package flat.viewing.scheduler

import flat.viewing.scheduler.api.bookings
import flat.viewing.scheduler.api.health
import flat.viewing.scheduler.api.properties
import flat.viewing.scheduler.api.users
import flat.viewing.scheduler.domain.booking.BookingManager
import flat.viewing.scheduler.domain.booking.BookingRepository
import flat.viewing.scheduler.domain.notifications.BookingNotifier
import flat.viewing.scheduler.domain.notifications.Notifier
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.users.UsersRepository
import flat.viewing.scheduler.infra.handleExceptions
import flat.viewing.scheduler.infra.notifications.BookingNotificationResolver
import flat.viewing.scheduler.infra.notifications.EmailNotifier
import flat.viewing.scheduler.infra.repo.InMemoryBookingRepository
import flat.viewing.scheduler.infra.repo.InMemoryPropertiesRepository
import flat.viewing.scheduler.infra.repo.InMemoryUsersRepository
import flat.viewing.scheduler.infra.serializers.InstantAdapter
import flat.viewing.scheduler.infra.validators.BookingPeriodValidator
import flat.viewing.scheduler.infra.validators.BookingPropertyValidator
import flat.viewing.scheduler.infra.validators.BookingTenantValidator
import flat.viewing.scheduler.infra.validators.BookingValidator
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import org.slf4j.event.Level
import java.text.DateFormat
import java.time.Clock
import java.time.Instant

fun Application.module() {
    val di = DI {
        bind<BookingRepository>() with singleton { InMemoryBookingRepository() }
        bind<PropertiesRepository>() with singleton { InMemoryPropertiesRepository() }
        bind<UsersRepository>() with singleton { InMemoryUsersRepository() }
        bind<BookingNotifier>() with singleton { BookingNotificationResolver(instance(), instance(), instance()) }
        bind<Notifier>() with singleton { EmailNotifier() }
        bind<BookingPeriodValidator>() with singleton { BookingPeriodValidator(Clock.systemUTC()) }
        bind<BookingTenantValidator>() with singleton { BookingTenantValidator(instance()) }
        bind<BookingValidator>() with singleton { BookingValidator(instance()) }
        bind<BookingPropertyValidator>() with singleton { BookingPropertyValidator(instance()) }
        bind<BookingManager>() with singleton {
            BookingManager(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
    }
    install(ContentNegotiation) {
        gson {
            registerTypeAdapter(Instant::class.java, InstantAdapter())
            setPrettyPrinting()
        }
    }
    install(StatusPages) { handleExceptions() }
    install(CallLogging) { level = Level.DEBUG }
    install(Routing) {
        val bookingRepository by di.instance<BookingRepository>()
        val propertiesRepository by di.instance<PropertiesRepository>()
        val usersRepository by di.instance<UsersRepository>()
        val bookingManager by di.instance<BookingManager>()

        health()
        properties(propertiesRepository)
        users(usersRepository)
        bookings(bookingRepository, bookingManager)
    }
}

fun main() {
    embeddedServer(Netty, 8080, module = Application::module).start()
}
