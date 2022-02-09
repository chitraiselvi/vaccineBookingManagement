INSERT INTO VACCINE (vaccine_id,vaccine_name,available_shots) VALUES
            ('PF001','Pfizer',100),
            ('AS001','AstraZeneca',200);

INSERT INTO SLOT (available_date,available_time,is_available) VALUES
            ('2022-02-19','10:00',false),
            ('2022-02-19','11:00',true),
            ('2022-02-19','13:00',true),
            ('2022-02-19','18:00',true),
            ('2022-02-20','10:00',true),
            ('2022-02-20','12:00',true),
            ('2022-02-20','14:00',true),
            ('2022-02-20','16:00',true);

INSERT INTO BOOKING (email_id,first_name,last_name,vaccine_name,mobile_no,slot_id) VALUES
    ('test@gmail.com','Ashok','Arichandran','Pfizer','1234567890',1);


