CREATE TABLE public.xa_clinic (
	commission_fee numeric(38, 2) NULL,
	medical_item_fee numeric(38, 2) NULL,
	sitting_fee numeric(38, 2) NULL,
	subscription_tier int4 NULL,
	subscription_valid_from timestamp(6) NULL,
	subscription_valid_to timestamp(6) NULL,
	code varchar(255) NULL,
	id varchar(255) NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE public.xa_employee (
	salary numeric(38, 2) NULL,
	tax_percentage numeric(38, 2) NULL,
	clinic_id varchar(255) NULL,
	code varchar(255) NULL,
	email_address varchar(255) NULL,
	first_name varchar(255) NULL,
	id varchar(255) NOT NULL,
	last_name varchar(255) NULL,
	"password" varchar(255) NULL,
	phone_number varchar(255) NULL,
	"role" varchar(255) NULL,
	status varchar(255) NULL,
	"type" varchar(255) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);


CREATE TABLE public.xa_item (
	expiry_date date NULL,
	quantity numeric(38, 2) NULL,
	clinic_id varchar(255) NULL,
	code varchar(255) NULL,
	id varchar(255) NOT NULL,
	"name" varchar(255) NULL,
	status varchar(255) NULL,
	unit_of_measurement varchar(255) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);


CREATE TABLE public.xa_patient (
	clinic_id varchar(255) NULL,
	code varchar(255) NULL,
	id varchar(255) NOT NULL,
	"name" varchar(255) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);


CREATE TABLE public.xa_treatment (
	price numeric(38, 2) NULL,
	clinic_id varchar(255) NULL,
	code varchar(255) NULL,
	id varchar(255) NOT NULL,
	"name" varchar(255) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (clinic_id) REFERENCES public.xa_clinic(id)
);


CREATE TABLE public.xa_visit (
	cancelled bool NULL,
	endtime timestamp(6) NULL,
	start_time timestamp(6) NULL,
	employee_id varchar(255) NULL,
	id varchar(255) NOT NULL,
	patient_id varchar(255) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (patient_id) REFERENCES public.xa_patient(id),
	FOREIGN KEY (employee_id) REFERENCES public.xa_employee(id)
);


CREATE TABLE public.xa_visit_treatment (
	description varchar(255) NULL,
	id varchar(255) NOT NULL,
	treatment_id varchar(255) NULL,
	visit_id varchar(255) NULL,
	PRIMARY KEY (id),
	UNIQUE (visit_id),
	FOREIGN KEY (visit_id) REFERENCES public.xa_visit(id),
	FOREIGN KEY (treatment_id) REFERENCES public.xa_treatment(id)
);


CREATE TABLE public.xa_attendance (
	clock_in timestamp(6) NULL,
	clock_out timestamp(6) NULL,
	employee_id varchar(255) NULL,
	id varchar(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (employee_id) REFERENCES public.xa_employee(id)
);