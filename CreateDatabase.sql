--Skrypt do dodawania relacji
create table Hotel_Pracownicy(
    pesel VARCHAR2(11) CONSTRAINT PK_Hotel_Pracownicy PRIMARY KEY,
    CONSTRAINT pesel_pracownika_dlugosc CHECK(pesel LIKE '___________'),
    imie VARCHAR2(30) NOT NULL,
    nazwisko VARCHAR2(30) NOT NULL,
    etat VARCHAR2(30) NOT NULL,
    placa NUMBER(7,2) CONSTRAINT placa_dodatnia CHECK(placa > 0) NOT NULL,
    data_urodzenia date NOT NULL,
    data_zatrudnienia date NOT NULL,
    umowa VARCHAR2(30) NOT NULL,
    adres VARCHAR2(50) NOT NULL,
    premia NUMBER(7,2) NULL CONSTRAINT premia_dodatnia CHECK(premia > 0)
);

create table Hotel_Klienci(
    pesel VARCHAR2(11) CONSTRAINT PK_Hotel_Klienci PRIMARY KEY,
    CONSTRAINT pesel_klienta_dlugosc CHECK(pesel LIKE '___________'),
    imie VARCHAR2(30) NOT NULL,
    nazwisko VARCHAR2(30) NOT NULL,
    numer_telefonu VARCHAR2(9) CONSTRAINT numer_telefonu_dlugosc9 CHECK(numer_telefonu LIKE '_________') NOT NULL,
    adres_zamieszkania VARCHAR(50) NOT NULL
);

create table Hotel_Hale_konferencyjne(
    numer_hali NUMBER(2) CONSTRAINT PK_Hala_Konferencyjna PRIMARY KEY,
    CONSTRAINT numer_hali_dodatni CHECK(numer_hali > 0),
    liczba_miejsc NUMBER(5) CONSTRAINT liczba_miejsc_dodatnia CHECK(liczba_miejsc > 0) NOT NULL
);

create table Hotel_Konferencje(
    id_konferencji NUMBER(6) GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_Konferencje PRIMARY KEY,
    nazwa VARCHAR(100) NOT NULL,
    data_konferencji DATE NOT NULL,
    liczba_osob NUMBER(5) CONSTRAINT liczba_osob_dodatnia CHECK(liczba_osob > 0) NOT NULL,
    hala_konferencyjna CONSTRAINT FK_Hala_konferencyjna REFERENCES Hotel_Hale_konferencyjne(numer_hali) ON DELETE CASCADE NOT NULL
);

create table Hotel_Nadzor_konferencji(
    id_konferencji CONSTRAINT FK_Nadzorowana_Konferencja REFERENCES Hotel_Konferencje(id_konferencji) ON DELETE CASCADE,
    pracownik CONSTRAINT FK_Pracownik_Nadzorujacy_Konferencje REFERENCES Hotel_Pracownicy(pesel) ON DELETE CASCADE,
    CONSTRAINT PK_Nadzor PRIMARY KEY(id_konferencji, pracownik)
);

create table Hotel_Rezerwacje(
    id_rezerwacji NUMBER(6) GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_Rezerwacje PRIMARY KEY,
    data_zameldowania date NOT NULL,
    termin_wymeldowania date NOT NULL,
    rabat NUMBER(4,2) NULL CONSTRAINT rabat_dodatni CHECK(rabat > 0),
    pracownik CONSTRAINT FK_Pracownik_Nadzorujacy_Rezerwacje REFERENCES Hotel_Pracownicy(pesel) ON DELETE CASCADE NOT NULL,
    klient CONSTRAINT FK_Klient REFERENCES Hotel_Klienci(pesel) ON DELETE CASCADE NOT NULL
);

create table Hotel_Pokoje(
    numer NUMBER(3) CONSTRAINT PK_Pokoje PRIMARY KEY,
    CONSTRAINT numer_pokoju_dodatni CHECK(numer > 0),
    cena_za_dobe NUMBER(7,2) CONSTRAINT cena_za_dobe_dodatnia CHECK(cena_za_dobe > 0) NOT NULL,
    liczba_lozek NUMBER(2) CONSTRAINT liczba_lozek_dodatnia CHECK(liczba_lozek > 0) NOT NULL,
    czy_telewizor NUMBER(1) CONSTRAINT czy_telewizor_binarny CHECK(czy_telewizor=0 or czy_telewizor=1) NOT NULL,
    czy_lazienka NUMBER(1) CONSTRAINT czy_lazienka_binarna CHECK(czy_lazienka=0 or czy_lazienka=1) NOT NULL
);

create table Hotel_Rezerwacja_pokoju(
    rezerwacja CONSTRAINT FK_Rezerwacje REFERENCES Hotel_Rezerwacje(id_rezerwacji) ON DELETE CASCADE,
    pokoj CONSTRAINT FK_Pokoj REFERENCES Hotel_Pokoje(numer) ON DELETE CASCADE,
    CONSTRAINT PK_Rezerwacja_Pokoju PRIMARY KEY(rezerwacja, pokoj)
);

create table Hotel_Zamowienia(
    id_zamowienia NUMBER(6) GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_Zamowienia PRIMARY KEY,
    data_zamowienia date NOT NULL,
    pracownik CONSTRAINT FK_Pracownik_Nadzorujacy_Zamowienie REFERENCES Hotel_Pracownicy(pesel) ON DELETE CASCADE NOT NULL
);

create table Hotel_Dania(
    id_dania NUMBER(3) GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_Dania PRIMARY KEY,
    nazwa VARCHAR2(50) NOT NULL,
    cena NUMBER(7,2) CONSTRAINT cena_dodatnia CHECK(cena > 0) NOT NULL,
    alergeny VARCHAR2(100)
);

create table Hotel_Zamowienie_dania(
    zamowienie CONSTRAINT FK_Zamowienia REFERENCES Hotel_Zamowienia(id_zamowienia) ON DELETE CASCADE,
    id_dania CONSTRAINT FK_Zamawianego_Dania REFERENCES Hotel_Dania(id_dania) ON DELETE CASCADE,
    CONSTRAINT PK_Zamowienia_Dania PRIMARY KEY(zamowienie, id_dania)
);

create table Hotel_Dostawcy(
    nip VARCHAR2(10) CONSTRAINT PK_Dostawcy PRIMARY KEY,
    CONSTRAINT np_dlugosc CHECK(nip LIKE '__________'),
    nazwa VARCHAR2(30) NOT NULL,
    adres VARCHAR(50) NOT NULL,
    numer_telefonu VARCHAR2(9) CONSTRAINT numer_telefonu_dostawcy_dlugosc9 CHECK(numer_telefonu LIKE '_________') NOT NULL
);

create table Hotel_Skladniki(
    nazwa VARCHAR2(30) CONSTRAINT PK_Skladniki PRIMARY KEY,
    stan_magazynu NUMBER(5) CONSTRAINT stan_magazynu_dodatni CHECK(stan_magazynu >= 0) NOT NULL,
    cena NUMBER(7,2) CONSTRAINT cena_skladniku_dodatnia CHECK(cena > 0) NOT NULL,
    dostawca CONSTRAINT FK_Dostawcy REFERENCES Hotel_Dostawcy(nip) ON DELETE CASCADE NOT NULL
);

create table Hotel_Sklad_dania(
    id_dania CONSTRAINT FK_Dania REFERENCES Hotel_Dania(id_dania) ON DELETE CASCADE,
    skladnik CONSTRAINT FK_Skladnika REFERENCES Hotel_Skladniki(nazwa) ON DELETE CASCADE,
    CONSTRAINT PK_Sklad_Dania PRIMARY KEY(id_dania, skladnik)
);

create or replace PROCEDURE dodajDostawce(vNip hotel_dostawcy.nip%TYPE, vNazwa hotel_dostawcy.nazwa%TYPE, 
    vAdres hotel_dostawcy.adres%TYPE, vNumer hotel_dostawcy.numer_telefonu%TYPE, bb NUMBER) IS
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_dostawcy(nip, nazwa, adres, numer_telefonu) VALUES(vNip, vNazwa, vAdres, vNumer);
    ELSE
        UPDATE hotel_dostawcy SET nazwa=vNazwa, adres=vAdres, numer_telefonu=vNumer WHERE nip=vNip;
    END IF;
END;

create or replace PROCEDURE dodajHale(vNumer hotel_hale_konferencyjne.numer_hali%TYPE,
vLiczba hotel_hale_konferencyjne.liczba_miejsc%TYPE, bb NUMBER) IS
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_hale_konferencyjne VALUES(vNumer, vLiczba);
    ELSE
        UPDATE hotel_hale_konferencyjne SET liczba_miejsc=vLiczba WHERE numer_hali=vNumer;
    END IF;
END;

create or replace PROCEDURE dodajKlienta(vPes hotel_klienci.pesel%TYPE, vIm hotel_klienci.imie%TYPE,
vNaz hotel_klienci.nazwisko%TYPE, vNum hotel_klienci.numer_telefonu%TYPE, vAd hotel_klienci.adres_zamieszkania%TYPE, bb NUMBER) IS
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_klienci VALUES(vPes, vIm, vNaz, vNum, vAd);
    ELSE
        UPDATE hotel_klienci SET imie=vIm, nazwisko=vNaz, numer_telefonu=vNum, adres_zamieszkania=vAd WHERE pesel=vPes;
    END IF;
END;

create or replace PROCEDURE dodajNadzorKonferencji(vId hotel_nadzor_konferencji.id_konferencji%TYPE, vPrac hotel_nadzor_konferencji.pracownik%TYPE) IS
BEGIN
    INSERT INTO hotel_nadzor_konferencji VALUES(vId, vPrac);
END;

create or replace PROCEDURE dodajPokoj(vNum hotel_pokoje.numer%TYPE, vCena hotel_pokoje.cena_za_dobe%TYPE,
vLicz hotel_pokoje.liczba_lozek%TYPE, vTel hotel_pokoje.czy_telewizor%TYPE, vLaz hotel_pokoje.czy_lazienka%TYPE, bb NUMBER) IS
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_pokoje VALUES(vNum, vCena, vLicz, vTel, vLaz);
    ELSE
        UPDATE hotel_pokoje SET cena_za_dobe=vCena, liczba_lozek=vLicz, czy_telewizor=vTel, czy_lazienka=vLaz WHERE numer=vNum;
    END IF;
END;

create or replace PROCEDURE dodajPracownika(vPes hotel_pracownicy.pesel%TYPE, vIm hotel_pracownicy.imie%TYPE,
vNaz hotel_pracownicy.nazwisko%TYPE, vEt hotel_pracownicy.etat%TYPE, vPla hotel_pracownicy.placa%TYPE,
vDatU hotel_pracownicy.data_urodzenia%TYPE, vDatZ hotel_pracownicy.data_zatrudnienia%TYPE, vUm hotel_pracownicy.umowa%TYPE,
vAd hotel_pracownicy.adres%TYPE, vPrem hotel_pracownicy.premia%TYPE, bb NUMBER) IS
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_pracownicy VALUES(vPes, vIm, vNaz, vEt, vPla, vDatU, vDatZ, vUm, vAd, vPrem);
    ELSE
        UPDATE hotel_pracownicy SET imie=vIm, nazwisko=vNaz, etat=vEt, placa=vPla, umowa=vUm, adres=vAd, premia=vPrem WHERE pesel=vPes;
    END IF;
END;

create or replace PROCEDURE dodajRezerwacjePokoju(vRez hotel_rezerwacja_pokoju.rezerwacja%TYPE, vPok hotel_rezerwacja_pokoju.pokoj%TYPE) IS
BEGIN
    INSERT INTO hotel_rezerwacja_pokoju VALUES(vRez, vPok);
END;

create or replace PROCEDURE dodajSklad(vId hotel_sklad_dania.id_dania%TYPE, vSk hotel_sklad_dania.skladnik%TYPE) IS
BEGIN
    INSERT INTO hotel_sklad_dania VALUES(vId, vSk);
END;

create or replace PROCEDURE dodajSkladnik(vNaz hotel_skladniki.nazwa%TYPE, vStan hotel_skladniki.stan_magazynu%TYPE,
vCen hotel_skladniki.cena%TYPE, vDost hotel_skladniki.dostawca%TYPE, bb NUMBER) IS
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_skladniki VALUES(vNaz, vStan, vCen, vDost);
    ELSE
        UPDATE hotel_skladniki SET stan_magazynu=vStan, cena=vCen, dostawca=vDost WHERE nazwa=vNaz;
    END IF;
END;

create or replace PROCEDURE dodajZamowienieDania(vZam hotel_zamowienie_dania.zamowienie%TYPE, vId hotel_zamowienie_dania.id_dania%TYPE) IS
BEGIN
    INSERT INTO hotel_zamowienie_dania VALUES(vZam, vId);
END;

create or replace FUNCTION dodajDanie(vId hotel_dania.id_dania%TYPE,vNazwa hotel_dania.nazwa%TYPE, vCena hotel_dania.cena%TYPE, 
vAlergeny hotel_dania.alergeny%TYPE, bb NUMBER) RETURN hotel_dania.id_dania%TYPE IS
vIde hotel_dania.id_dania%TYPE;
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_dania(nazwa, cena, alergeny) VALUES(vNazwa, vCena, vAlergeny) RETURNING id_dania INTO vIde;
        RETURN vIde;
    ELSE
        UPDATE hotel_dania SET nazwa=vNazwa, cena=vCena, alergeny=vAlergeny WHERE id_dania=vId;
        RETURN null;
    END IF;
END;

create or replace FUNCTION dodajKonferencje(vId hotel_konferencje.id_konferencji%TYPE, vNaz hotel_konferencje.nazwa%TYPE, vDat hotel_konferencje.data_konferencji%TYPE,
vLicz hotel_konferencje.liczba_osob%TYPE, vHala hotel_konferencje.hala_konferencyjna%TYPE, bb NUMBER) RETURN hotel_konferencje.id_konferencji%TYPE IS
vIde hotel_konferencje.id_konferencji%TYPE;
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_konferencje(nazwa, data_konferencji, liczba_osob, hala_konferencyjna) VALUES(vNaz, vDat, vLicz, vHala)
        RETURNING id_konferencji INTO vIde;
        RETURN vIde;
    ELSE
        UPDATE hotel_konferencje SET nazwa=vNaz, data_konferencji=vDat, liczba_osob=vLicz, hala_konferencyjna=vHala WHERE id_konferencji=vId;
        RETURN null;
    END IF;
END;

create or replace FUNCTION dodajRezerwacje(vId hotel_rezerwacje.id_rezerwacji%TYPE, vDat hotel_rezerwacje.data_zameldowania%TYPE, vTerm hotel_rezerwacje.termin_wymeldowania%TYPE,
vRab hotel_rezerwacje.rabat%TYPE, vPrac hotel_rezerwacje.pracownik%TYPE, vKli hotel_rezerwacje.klient%TYPE, bb NUMBER) RETURN hotel_rezerwacje.id_rezerwacji%TYPE IS
vIde hotel_rezerwacje.id_rezerwacji%TYPE;
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_rezerwacje(data_zameldowania, termin_wymeldowania, rabat, pracownik, klient) VALUES (vDat, vTerm, vRab, vPrac, vKli)
        RETURNING id_rezerwacji INTO vIde;
        RETURN vIde;
    ELSE
        UPDATE hotel_rezerwacje SET data_zameldowania=vDat, termin_wymeldowania=vTerm, rabat=vRab, pracownik=vPrac, klient=vKli WHERE id_rezerwacji=vId;
        RETURN null;
    END IF;
END;

create or replace FUNCTION dodajZamowienie(vId hotel_zamowienia.id_zamowienia%TYPE, vDat hotel_zamowienia.data_zamowienia%TYPE,
vPrac hotel_zamowienia.pracownik%TYPE, bb NUMBER) RETURN hotel_zamowienia.id_zamowienia%TYPE IS
vIde hotel_zamowienia.id_zamowienia%TYPE;
BEGIN
    IF bb < 1 THEN
        INSERT INTO hotel_zamowienia(data_zamowienia, pracownik) VALUES(vDat, vPrac) 
        RETURNING id_zamowienia INTO vIde;
        RETURN vIde;
    ELSE
        UPDATE hotel_zamowienia SET data_zamowienia=vDat, pracownik=vPrac WHERE id_zamowienia=vIde;
        RETURN null;
    END IF;
END;

create or replace FUNCTION dostepnoscDania(vId hotel_dania.id_dania%TYPE) RETURN VARCHAR2 IS
BEGIN
    FOR record in (SELECT stan_magazynu from hotel_skladniki where nazwa IN (SELECT skladnik FROM hotel_sklad_dania where id_dania=vId))
    LOOP 
        IF record.stan_magazynu < 1 THEN
            RETURN 'Niedostepne';
        END IF;
    END LOOP;
    RETURN 'Dostepne';
END;

create or replace function sprawdzPesel(peselToCheck hotel_klienci.pesel%type) RETURN NUMBER IS
i number;
begin
    select 1 into i from hotel_klienci where pesel = peselToCheck;
    if (i=1) then
        return 1;
    else
        return 0;
    end if;
end;

create or replace FUNCTION sprawdzPokoj(rezID hotel_rezerwacje.id_rezerwacji%TYPE, 
pID hotel_rezerwacja_pokoju.pokoj%TYPE, checkDateZameldowania hotel_rezerwacje.data_zameldowania%TYPE, 
checkDateWymeldowania hotel_rezerwacje.data_zameldowania%TYPE) RETURN NUMBER IS
CURSOR rs IS SELECT r.data_zameldowania, r.termin_wymeldowania FROM hotel_rezerwacja_pokoju rp inner join hotel_rezerwacje r on (rp.rezerwacja=r.id_rezerwacji) where rp.pokoj=pID and r.id_rezerwacji!=rezID;
datZ DATE;
datW DATE;
BEGIN
    OPEN rs;
    LOOP
        FETCH rs INTO datZ, datW;
        IF( (checkDateZameldowania >= datZ AND checkDateWymeldowania <= datW) OR (checkDateZameldowania <= datZ AND checkDateWymeldowania >= datZ) OR (checkDateZameldowania <= datW AND checkDateWymeldowania >= datW) OR (checkDateZameldowania <= datZ AND checkDateWymeldowania >= datW)) THEN
            CLOSE rs;
            RETURN 0;
        ELSE
            NULL;
        END IF;
        EXIT WHEN rs%NOTFOUND;
    END LOOP;
    CLOSE rs;
    RETURN 1;
END;

commit;
