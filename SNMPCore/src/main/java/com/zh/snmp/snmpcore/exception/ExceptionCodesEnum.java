package com.zh.snmp.snmpcore.exception;

public enum ExceptionCodesEnum {

    /**
     * Ez egy általános kivétel típus, valójában nem kéne használni.
     * Helyette valami konkrétabb kivétel típust kell definiálni.
     */
    UnknownExceptionType,
    IOException,
    RemoteException,
    PersistenceException,
    SQLException,
    
    /**
     * Ilyen kóddal akkor dobunk kivételt, ha nem megfelelő módon,
     * vagy helyen használnak egyet a saját annotációink közül.
     */
    InvalidAnnotationUsage,

    /**
     * Akkor dobódik ilyen kivétel, ha hiányzik egy annotáció
     * valamilyen metódusról, vagy field-ről.
     */
    AnnotationMissing,
    DataInconsistence,
    ConfigurationException,
    DBFException,
    StateException,

    /**
     * Ilyen kivétel akkor dobódik, ha a rossz konfiguráció van beállítva a devicehoz
     */
    WrongDeviceType,
    Unsupported,
    
    /**
     * Ilyen kóddal akkor dobunk kivételt,
     * ha nem megfelelő típusú objektum érkezik valahol.
     */
    NotSupportedObjectType,

    /**
     * Ilyen kivételt dobunk, ha olyan definíciót
     * próbál publikálni, amihez még nem tartozik egyetlen partner sem. 
     */
    DefinitionHasNoPartners,    
    /**
     * Ezt a kivételt az xml sorosítása során keletkezett hibákat jelzi
     */
    MarshallingException,

    /**
     * Akkor dobódik ilyen kivétel, ha ismerelen típusú megjegyzést
     * próbálunk megjeleníteni a felületen.
     *
     */
    IsmeretlenMegjegyzesTipus,

    /**
     * Akkor dobódik ilyen kivétel,
     * ha mentéskor a wrapped objektum JPAVersion azonosítója nem egyezik meg a K11Entity verziójával,
     * azaz a K11Entity mentve volt azóta, hogy a wrapped objektum kiolvasásra került
     *
     */
    JPAVerzioHiba,
    
    /**
     * Ez a kivétel egy hiányzó metódus argumentumot jelöl. (Pl.: null argumentum, pedig nem lehetne null...)
     */
    ArgumentMissing,

    /**
     * Példányosítás során keletkezett hibákat jelző kivétel.
     */
    PeldanyositasException;




}
