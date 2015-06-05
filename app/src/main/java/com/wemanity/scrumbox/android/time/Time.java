package com.wemanity.scrumbox.android.time;

public class Time {
    private Sign sign;
    private long totalMillis;
    private long minute;
    private long seconde;
    private long millis;

    private Time(Builder builder) {
        sign = builder.sign;
        totalMillis = builder.totalMillis;
        minute = builder.minute;
        seconde = builder.seconde;
        millis = builder.millis;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Sign getSign() {
        return sign;
    }

    public long getTotalMillis() {
        return totalMillis;
    }

    public long getMinute() {
        return minute;
    }

    public long getSeconde() {
        return seconde;
    }

    public long getMillis() {
        return millis;
    }

    public static final class Builder {
        private Sign sign;
        private long totalMillis;
        private long minute;
        private long seconde;
        private long millis;

        private Builder() {
        }

        public Builder sign(Sign sign) {
            this.sign = sign;
            return this;
        }

        public Builder totalMillis(long totalMillis) {
            this.totalMillis = totalMillis;
            return this;
        }

        public Builder minute(long minute) {
            this.minute = minute;
            return this;
        }

        public Builder seconde(long seconde) {
            this.seconde = seconde;
            return this;
        }

        public Builder millis(long millis) {
            this.millis = millis;
            return this;
        }

        public Time build() {
            return new Time(this);
        }
    }
}
