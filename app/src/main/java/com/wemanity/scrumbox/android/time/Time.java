package com.wemanity.scrumbox.android.time;

import com.wemanity.scrumbox.android.Sign;

public class Time {
    private Sign sign;
    private long totalMillis;
    private long minute;
    private long seconde;
    private long millis;

    public Time(){

    }

    private Time(Builder builder) {
        setSign(builder.sign);
        setTotalMillis(builder.totalMillis);
        setMinute(builder.minute);
        setSeconde(builder.seconde);
        setMillis(builder.millis);
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public void setTotalMillis(long totalMillis) {
        this.totalMillis = totalMillis;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public void setSeconde(long seconde) {
        this.seconde = seconde;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public long getTotalMillis() {
        return totalMillis;
    }

    public Sign getSign() {
        return sign;
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

        public Builder() {
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
