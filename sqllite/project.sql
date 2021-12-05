drop table if exists userManagment;
create table if not exists userManagment
(
    username primary key,
    firstName text,
    lastName text,
    password text



);

insert into userManagment (username, firstName, lastName, password)
values ('drubinivitz0', 'Diann', 'Rubinivitz', 'NV2fmV56');
insert into userManagment (username, firstName, lastName, password)
values ('hkrook1', 'Hortense', 'Krook', 'RVlBBbtijfU');
insert into userManagment (username, firstName, lastName, password)
values ('rcarvil2', 'Robenia', 'Carvil', 'd2iR2V');
insert into userManagment (username, firstName, lastName, password)
values ('lbale3', 'Leisha', 'Bale', 'GuMoyAqkoB');
insert into userManagment (username, firstName, lastName, password)
values ('hlarimer4', 'Hadria', 'Larimer', 'R4hYZgu5');