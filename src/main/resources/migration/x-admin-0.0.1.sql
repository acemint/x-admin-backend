-- MANUAL VERSIONIN RULEG:
  -- 1. DURING NEW COMMIT:
    -- If we have a changes of schema, once we commit the changes to Github, we need to UPDATE the table version to the given commitNumber

  -- 2. WHEN RUNNING THE CHANGES:
    -- When we run the DB Migration manually, we need to also ALTER the version. This helps us to check which version it is currently in

INSERT INTO public.version(commit_id, description)
VALUES ('d97c50a48432038bdf3e123cdc2207a60a8e6668', 'Initialize xa_clinic and xa_member table');

CREATE TABLE public.version (
    commit_id VARCHAR(255) NOT NULL,
    description TEXT
)

CREATE TABLE public.xa_clinic (
    id varchar(255) NOT NULL,
    created_date timestamp(6),
    created_by varchar(255),
    last_modified_date timestamp(6),
    last_modified_by varchar(255),
    code varchar(255) UNIQUE NOT NULL,
    "name" varchar(255) NOT NULL,
    subscription_tier int4,
    subscription_valid_from timestamp(6),
    subscription_valid_to timestamp(6),
	commission_fee numeric(38, 2),
	medical_item_fee numeric(38, 2),
	sitting_fee numeric(38, 2),
	PRIMARY KEY (id)
);

CREATE SEQUENCE public.clinic_sequence AS bigint;

CREATE TABLE public.xa_member (
    id varchar(255) NOT NULL,
    created_date timestamp(6),
    created_by varchar(255),
    last_modified_date timestamp(6),
    last_modified_by varchar(255),
    code varchar(255) UNIQUE NOT NULL,
    satu_sehat_patient_reference_id varchar(255),
    satu_sehat_practitioner_reference_id varchar(255),
    clinic_username varchar(255) UNIQUE NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255),
    nik varchar(255),
    mother_nik varchar(255),
	email_address varchar(255) NOT NULL,
	phone_number varchar(255),
	address varchar(255),
	date_of_birth date,
	gender varchar(255),
	"password" varchar(255),
	"role" varchar(255) NOT NULL,
    activation_status varchar(255) NOT NULL,
    admin_type varchar(255),
	practitioner_type varchar(255),
	practitioner_number varchar(255),
	practitioner_practice_license varchar(255),
	practitioner_salary numeric(38, 2),
	practitioner_tax_percentage numeric(38, 2),
	clinic_id varchar(255),
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);

CREATE SEQUENCE public.member_sequence AS bigint;


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
	patient_id varchar(255) NOT NULL,
	practitioner_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (patient_id) REFERENCES public.xa_member(id),
	FOREIGN KEY (practitioner_id) REFERENCES public.xa_member(id)
);

CREATE TABLE public.xa_visit_history (
	id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
	status varchar(50) NOT NULL,
	start_time timestamp(6) NOT NULL,
	end_time timestamp(6) NOT NULL,
	visit_id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (visit_id) REFERENCES public.xa_visit(id)
);

CREATE SEQUENCE public.visit_sequence AS bigint;

INSERT INTO public.version(commit_id, description)
VALUES ('b1c35e7dc9327bc8a91624366754f8257309441e', 'Initialize xa_visit table');

ALTER TABLE xa_clinic ADD COLUMN satu_sehat_clinic_reference_id VARCHAR(255) NOT NULL;
ALTER TABLE xa_visit ALTER COLUMN end_time DROP NOT NULL;
ALTER TABLE xa_visit_history ALTER COLUMN end_time DROP NOT NULL;

INSERT INTO public.version(commit_id, description)
VALUES ('8c496e9e57977abd65420eab0ae481784db59039', 'Added column for Satu Sehat Reference ID in SatuSehat and remove not null in xa_visit');

DROP TABLE xa_visit_history;
ALTER TABLE xa_visit ADD COLUMN room VARCHAR(255);
ALTER TABLE xa_visit ADD COLUMN room_description VARCHAR(255);
CREATE TABLE public.xa_room (
    id varchar(255) NOT NULL,
    created_date timestamp(6) null,
    created_by varchar(255) null,
    last_modified_date timestamp(6) null,
    last_modified_by varchar(255) null,
    code varchar(255) not null,
    name varchar(255) not null,
    description varchar(255),
    satu_sehat_room_reference_id varchar(255) NOT NULL,
    clinic_id varchar(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);
ALTER TABLE xa_visit ADD COLUMN room_id varchar(255) NOT NULL;
ALTER TABLE xa_visit ADD CONSTRAINT xa_visit_room_id_fkey FOREIGN KEY (room_id) REFERENCES xa_room(id);

CREATE SEQUENCE public.room_sequence AS bigint;

INSERT INTO public.version(commit_id, description)
VALUES ('35f671117981eda75ca3858c7b282e2e47abe0ee', 'Remove visit history and add room and room description in xa_visit and add xa_room');

ALTER TABLE xa_visit ADD COLUMN satu_sehat_encounter_reference_id VARCHAR(255);
ALTER TABLE xa_visit DROP COLUMN room;
ALTER TABLE xa_visit DROP COLUMN room_description;

INSERT INTO public.version(commit_id, description)
VALUES ('22817ee9-329f-4e75-90db-199fa14300af', 'Remove unused column and add reference_id in xa_visit');



-- TODO: Later implement this
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