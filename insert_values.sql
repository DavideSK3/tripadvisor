
insert into STATES VALUES('Italia');
insert into STATES VALUES('Francia');
insert into STATES VALUES('Regno Unito');
insert into STATES VALUES('Stati Uniti America');
insert into STATES VALUES('Spagna');
insert into STATES VALUES('Russia');
insert into STATES VALUES('Germania');
insert into STATES VALUES('Cina');
insert into STATES VALUES('Giappone');

insert into REGIONS VALUES('Italia', 'Lombardia');
insert into REGIONS VALUES('Italia', 'Trentino Alto-Adige');
insert into REGIONS VALUES('Italia', 'Veneto');
insert into REGIONS VALUES('Italia', 'Friuli Venezia-Giulia');
insert into REGIONS VALUES('Italia', 'Piemonte');
insert into REGIONS VALUES('Italia', 'Lazio');
insert into REGIONS VALUES('Italia', 'Sicilia');
insert into REGIONS VALUES('Italia', 'Basilicata');
insert into REGIONS VALUES('Italia', 'Puglia');
insert into REGIONS VALUES('Italia', 'Molise');
insert into REGIONS VALUES('Regno Unito', 'Inghilterra');
insert into REGIONS VALUES('Regno Unito', 'Galles');
insert into REGIONS VALUES('Regno Unito', 'Scozia');
insert into REGIONS VALUES('Regno Unito', 'Irlanda del Nord');
insert into REGIONS VALUES('Francia', 'Ile de France');
insert into REGIONS VALUES('Francia', 'Bordeaux');
insert into REGIONS VALUES('Stati Uniti America', 'Michigan');
insert into REGIONS VALUES('Stati Uniti America', 'Washington');
insert into REGIONS VALUES('Stati Uniti America', 'New York');
insert into REGIONS VALUES('Stati Uniti America', 'Texas');


insert into CITIES VALUES('Italia', 'Lombardia', 'Mantova');
insert into CITIES VALUES('Italia', 'Lombardia', 'Milano');
insert into CITIES VALUES('Italia', 'Trentino Alto-Adige', 'Trento');
insert into CITIES VALUES('Italia', 'Trentino Alto-Adige', 'Bolzano');
insert into CITIES VALUES('Italia', 'Veneto', 'Verona');
insert into CITIES VALUES('Italia', 'Veneto', 'Venezia');
insert into CITIES VALUES('Italia', 'Veneto', 'Padova');
insert into CITIES VALUES('Italia', 'Lazio', 'Roma');
insert into CITIES VALUES('Italia', 'Piemonte', 'Torino');
insert into CITIES VALUES('Italia', 'Friuli Venezia-Giulia', 'Pordenone');
insert into CITIES VALUES('Italia', 'Friuli Venezia-Giulia', 'Trieste');
insert into CITIES VALUES('Italia', 'Sicilia', 'Palermo');

insert into CITIES VALUES('Regno Unito', 'Inghilterra', 'Londra');
insert into CITIES VALUES('Regno Unito', 'Inghilterra', 'Liverpool');
insert into CITIES VALUES('Regno Unito', 'Inghilterra', 'Manchester');
insert into CITIES VALUES('Regno Unito', 'Scozia', 'Edimburgo');

insert into CITIES VALUES('Francia', 'Ile de France', 'Parigi');


insert into USERS VALUES (default, 'Gabriele', 'Cesa', 'prova', 'gabriele.cesa@gmail.com', 'u');
insert into USERS VALUES (default, 'Gabriele', 'Cesa', 'prova', 'gabriele.cesa@studenti.unitn.it', 'a');


insert into RESTAURANTS VALUES
(
    default,
    'Pizza&Pizza',
    'Vieni a mangiare da noi la pizza, è buona!',   
    'www.ma_che_ne_so.it',
    2,
    10.0,
    10.0,
    10.0,
    10.0,
    10.0,
    null,
    1,
    'via Taramelli 24',
    12345678.0,
    12345678.0,
    2,
    50,
    'Italia',
    'Trentino Alto-Adige',
    'Trento'
);

insert into RESTAURANTS VALUES
(
    default,
    'Gelato&Gelato',
    'Vieni a mangiare da noi il gelato, è buono!\n(Non siamo in alcun modo associati con Pizza&Pizza)',   
    'www.ma_che_ne_so_io.it',
    3,
    15.0,
    14.0,
    13.0,
    12.0,
    11.0,
    null,
    1,
    'via Taramelli 14',
    0.788036490359,
    0.18845762622895723126,
    1,
    100,
    'Italia',
    'Lombardia',
    'Mantova'
);

insert into RESTAURANTS VALUES
(
    default,
    'VivaIlKebab',
    'Vieni a mangiare da noi il kebab, è buono!',   
    'www.ma_che_buono_il_kebab.it',
    4,
    13.5,
    17.0,
    15.0,
    10.0,
    18.0,
    null,
    1,
    'via Dalle Balle 1',
    0.793499301105,
    0.160401622564,
    4,
    10,
    'Italia',
    'Lombardia',
    'Milano'
);


insert into RESTAURANTS VALUES
(
    default,
    'SushiRadioattivo',
    'Assaggia il nostro sushi speciale!',   
    'www.sushi.rom',
    8,
    37.5,
    46.0,
    43.0,
    30.0,
    28.0,
    null,
    1,
    'via Cernobyl 1986',
    0.793499301405,
    0.160401622964,
    12,
    45,
    'Italia',
    'Lombardia',
    'Milano'
);




insert into cuisines values (default, 'europeo');
insert into cuisines values (default, 'cinese');
insert into cuisines values (default, 'italiano');
insert into cuisines values (default, 'giapponese');
insert into cuisines values (default, 'francese');
insert into cuisines values (default, 'pizza');

insert into RESTAURANTS_CUISINES values(1, 3);
insert into RESTAURANTS_CUISINES values(1, 6);
insert into RESTAURANTS_CUISINES values(2, 3);


