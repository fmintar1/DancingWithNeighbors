package rocks.zipcode.domain.enumeration;

/**
 * The Styles enumeration.
 */
public enum Styles {
    BACHATA("Bachata"),
    BALLET("Ballet"),
    BOLERO("Bolero"),
    CHACHA("ChaCha"),
    FOXTROT("Foxtrot"),
    HUSTLE("Hustle"),
    JITTERBUG("Jitterbug"),
    JIVE("Jive"),
    KIZOMBA("Kizomba"),
    MAMBO("Mambo"),
    MERENGUE("Merengue"),
    RUMBA("Rumba"),
    SALSA("Salsa"),
    SAMBA("Samba"),
    SLOWDANCE("Slowdance"),
    SWING("Swing"),
    TANGO("Tango"),
    TWIST("Twist"),
    WALTZ("Waltz"),
    ZOUK("Zouk");

    private final String value;

    Styles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
