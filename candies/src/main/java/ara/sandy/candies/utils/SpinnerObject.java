package ara.sandy.candies.utils;

public class SpinnerObject {

    private String Code;
    private String Value;
    private String extras;

    public SpinnerObject(String Code , String Value ) {
        this.Code = Code;
        this.Value = Value;
    }

    public SpinnerObject(String Code , String Value, String extras) {
        this.Code = Code;
        this.Value = Value;
        this.extras = extras;
    }

    public String getId () {
        return Code;
    }

    public String getExtras() {
        return extras;
    }

    public String getValue () {
        return Value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}