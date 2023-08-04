package my.restaurant.modal;

public enum Theme {
    LIGHT {
        @Override
        public String toString() {
            return "light";
        }
    },
    DARK {
        @Override
        public String toString() {
            return "dark";
        }
    }
}
