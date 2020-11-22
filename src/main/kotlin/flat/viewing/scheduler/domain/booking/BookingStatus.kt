package flat.viewing.scheduler.domain.booking

enum class BookingStatus(value: String) {
    REQUESTED("Запрошено"),
    APPROVED("Подтверждено"),
    REJECTED("Отклонено"),
    DELETED("Удалено"),
    /**/;
}
