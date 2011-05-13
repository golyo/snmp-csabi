/*
 *  *  Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Sonrisa Informatikai Kft. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sonrisa.
 *
 * SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.zh.snmp.snmpcore.exception;

/**
 * Az egyes hiba tipusokat azonosito kodokat tartalmazo enumeracio.
 * Igeny szerint bovitendo.
 *
 * @author Joe
 */
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
     * Ilyen kivétel akkor dobódik, ha a kódtár használtat
     * közben történik valamilyen hiba. Például olyan kódtárból
     * próbál olvasni, ami nincs, vagy olyan kódot, ami nem található
     * meg az adott kódtárban.
     */
    KodtarException,
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
