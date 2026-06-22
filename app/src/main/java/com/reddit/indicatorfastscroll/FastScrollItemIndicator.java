package com.reddit.indicatorfastscroll;

/* Cleaned from decompiled Kotlin. Sealed-style hierarchy with Icon + Text. */
public abstract class FastScrollItemIndicator {

    public static final class Icon extends FastScrollItemIndicator {
        private final int iconRes;

        public Icon(int iconRes) {
            super();
            this.iconRes = iconRes;
        }

        public final int getIconRes() {
            return this.iconRes;
        }

        public final Icon copy(int iconRes) {
            return new Icon(iconRes);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            return (other instanceof Icon) && this.iconRes == ((Icon) other).iconRes;
        }

        @Override
        public int hashCode() {
            return this.iconRes;
        }

        @Override
        public String toString() {
            return "Icon(iconRes=" + this.iconRes + ")";
        }
    }

    public static final class Text extends FastScrollItemIndicator {
        private final String text;

        public Text(String text) {
            super();
            this.text = text;
        }

        public final String getText() {
            return this.text;
        }

        public final Text copy(String text) {
            return new Text(text);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            return (other instanceof Text)
                    && (this.text == null ? ((Text) other).text == null : this.text.equals(((Text) other).text));
        }

        @Override
        public int hashCode() {
            String str = this.text;
            return str != null ? str.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Text(text=" + this.text + ")";
        }
    }

    private FastScrollItemIndicator() {
    }
}
