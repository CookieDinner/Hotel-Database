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
    hala_konferencyjna CONSTRAINT FK_Hala_konferencyjna REFERENCES Hotel_Hale_konferencyjne(numer_hali) NOT NULL
);

create table Hotel_Nadzor_konferencji(
    id_konferencji CONSTRAINT FK_Nadzorowana_Konferencja REFERENCES Hotel_Konferencje(id_konferencji),
    pracownik CONSTRAINT FK_Pracownik_Nadzorujacy_Konferencje REFERENCES Hotel_Pracownicy(pesel),
    CONSTRAINT PK_Nadzor PRIMARY KEY(id_konferencji, pracownik)
);

create table Hotel_Rezerwacje(
    id_rezerwacji NUMBER(6) GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_Rezerwacje PRIMARY KEY,
    data_zameldowania date NOT NULL,
    termin_wymeldowania date NOT NULL,
    rabat NUMBER(4,2) NULL CONSTRAINT rabat_dodatni CHECK(rabat > 0),
    pracownik CONSTRAINT FK_Pracownik_Nadzorujacy_Rezerwacje REFERENCES Hotel_Pracownicy(pesel) NOT NULL,
    klient CONSTRAINT FK_Klient REFERENCES Hotel_Klienci(pesel) NOT NULL
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
    rezerwacja CONSTRAINT FK_Rezerwacje REFERENCES Hotel_Rezerwacje(id_rezerwacji),
    pokoj CONSTRAINT FK_Pokoj REFERENCES Hotel_Pokoje(numer),
    CONSTRAINT PK_Rezerwacja_Pokoju PRIMARY KEY(rezerwacja, pokoj)
);

create table Hotel_Zamowienia(
    id_zamowienia NUMBER(6) GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_Zamowienia PRIMARY KEY,
    data_zamowienia date NOT NULL,
    pracownik CONSTRAINT FK_Pracownik_Nadzorujacy_Zamowienie REFERENCES Hotel_Pracownicy(pesel) NOT NULL
);

create table Hotel_Dania(
    id_dania NUMBER(3) GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_Dania PRIMARY KEY,
    nazwa VARCHAR2(50) NOT NULL,
    cena NUMBER(7,2) CONSTRAINT cena_dodatnia CHECK(cena > 0) NOT NULL,
    alergeny VARCHAR2(100)
);

create table Hotel_Zamowienie_dania(
    zamowienie CONSTRAINT FK_Zamowienia REFERENCES Hotel_Zamowienia(id_zamowienia),
    id_dania CONSTRAINT FK_Zamawianego_Dania REFERENCES Hotel_Dania(id_dania),
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
    dostawca CONSTRAINT FK_Dostawcy REFERENCES Hotel_Dostawcy(nip) NOT NULL
);

create table Hotel_Sklad_dania(
    id_dania CONSTRAINT FK_Dania REFERENCES Hotel_Dania(id_dania),
    skladnik CONSTRAINT FK_Skladnika REFERENCES Hotel_Skladniki(nazwa),
    CONSTRAINT PK_Sklad_Dania PRIMARY KEY(id_dania, skladnik)
);

insert into hotel_pracownicy values (87071975664, 'Slawomir', 'Bubel', 'Menedzer', 2734, '1987-07-19', '2020-01-12', 'Na stale', 'Flanelcka 54/7A', null);
insert into hotel_pracownicy values (97081575664, 'Jaroslaw', 'Bubel', 'Pod Menedzer', 2734, '1997-08-15', '2020-01-13', 'Na stale', 'Flanelcka 60/9A', null);
insert into hotel_pracownicy values (68061975664, 'Mariusz', 'Gruszka', 'Kelner', 2734, '1968-06-19', '2020-01-14', 'Na stale', 'Flanelcka 25/1B', null);
insert into hotel_pracownicy values (99071975664, 'Janina', 'Pianina', 'Sekretarka', 2734, '1999-07-19', '2020-01-15', 'Na stale', 'Flanelcka 1/1A', null);
insert into hotel_pracownicy values (75072575664, 'Arek', 'Pantalon', 'Kucharz', 2734, '1975-07-25', '2020-01-16', 'Na stale', 'Flanelcka 99/99D', null);

insert into hotel_hale_konferencyjne values (1, 12);

commit;