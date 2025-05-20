package objects;

//LegalForm je enum, ktorý definuje typ subjektu: NATURAL - fyzická osoba, LEGAL - právnická osoba.
//Používa sa v triede Person na definovanie toho, kto je poistník, a na riadenie logiky v MasterVehicleContract.
public enum LegalForm {
    NATURAL,
    LEGAL
}
