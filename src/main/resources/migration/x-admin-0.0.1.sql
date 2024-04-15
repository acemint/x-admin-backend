CREATE TABLE public.xa_clinic (
    id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
    code varchar(255) NOT NULL,
    "name" varchar(255) NOT NULL,
    subscription_tier int4 NULL,
    subscription_valid_from timestamp(6) NULL,
    subscription_valid_to timestamp(6) NULL,
	commission_fee numeric(38, 2) NULL,
	medical_item_fee numeric(38, 2) NULL,
	sitting_fee numeric(38, 2) NULL,
	PRIMARY KEY (id)
);

CREATE SEQUENCE public.clinic_sequence AS bigint;
ALTER TABLE xa_clinic ADD UNIQUE(code);

CREATE TABLE public.xa_member (
    id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
    code varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NULL,
	email_address varchar(255) NOT NULL,
	phone_number varchar(255) NOT NULL,
	address varchar(255) NOT NULL,
	age int4 NOT NULL,
	gender varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	"role" varchar(255) NOT NULL,
	"type" varchar(255) NOT,
    status varchar(255) NOT NULL,
	salary numeric(38, 2) NULL,
	tax_percentage numeric(38, 2) NULL,
	doctor_number varchar(255),
	practice_license varchar(255),
	clinic_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);

CREATE SEQUENCE public.member_sequence AS bigint;
ALTER TABLE xa_member ADD UNIQUE(code);
ALTER TABLE xa_member ADD UNIQUE(username);


CREATE TABLE public.xa_item (
    id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
    code varchar(255) NOT NULL,
    "name" varchar(255) NOT NULL,
	expiry_date date NULL,
	quantity numeric(38, 2) NOT NULL,
	unit_of_measurement varchar(255) NOT NULL,
	status varchar(255) NOT NULL,
	clinic_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);

ALTER TABLE xa_item ADD UNIQUE(code);


CREATE TABLE public.xa_patient (
    id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
	code varchar(255) NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NULL,
	email_address varchar(255) NOT NULL,
	phone_number varchar(255) NOT NULL,
	address varchar(255) NOT NULL,
	age int4 NOT NULL,
	gender varchar(255) NOT NULL,
	clinic_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);

CREATE SEQUENCE public.patient_sequence AS bigint;
ALTER TABLE xa_patient ADD UNIQUE(code);


CREATE TABLE public.xa_visit (
	id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
	code varchar(255) NOT NULL,
	status varchar(50) NOT NULL,
	start_time timestamp(6) NOT NULL,
	end_time timestamp(6) NOT NULL,
	member_id varchar(255) NOT NULL,
	patient_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (patient_id) REFERENCES public.xa_patient(id),
	FOREIGN KEY (member_id) REFERENCES public.xa_member(id)
);

CREATE SEQUENCE public.visit_sequence AS bigint;


CREATE TABLE public.xa_treatment (
	id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
	code varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	price numeric(38, 2) NOT NULL,
	clinic_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);

ALTER TABLE xa_treatment ADD UNIQUE(code);


CREATE TABLE public.xa_visit_treatment (
    id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
	description text NULL,
	treatment_id varchar(255) NOT NULL,
	visit_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (visit_id),
	FOREIGN KEY (visit_id) REFERENCES public.xa_visit(id),
	FOREIGN KEY (treatment_id) REFERENCES public.xa_treatment(id)
);


CREATE TABLE public.xa_attendance (
    id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
	clock_in timestamp(6) NOT NULL,
	clock_out timestamp(6) NOT NULL,
	member_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (member_id) REFERENCES public.xa_member(id)
);