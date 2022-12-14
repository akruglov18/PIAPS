toc.dat                                                                                             0000600 0004000 0002000 00000013035 14343605161 0014444 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        PGDMP                           z            piaps    15.1    15.1                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                    0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                    0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                    1262    16398    piaps    DATABASE     y   CREATE DATABASE piaps WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';
    DROP DATABASE piaps;
                postgres    false         ?            1259    16426 	   msg_table    TABLE     /  CREATE TABLE public.msg_table (
    msgid character varying(64) NOT NULL,
    useridfrom character varying(64) NOT NULL,
    useridto character varying(64) NOT NULL,
    strtimestamp character varying(64) NOT NULL,
    theme character varying(256) NOT NULL,
    body character varying(1024) NOT NULL
);
    DROP TABLE public.msg_table;
       public         heap    postgres    false         ?            1259    16443    requests_table    TABLE     ?  CREATE TABLE public.requests_table (
    reqid character varying(64) NOT NULL,
    userid character varying(64) NOT NULL,
    status character varying(32) NOT NULL,
    adtnum integer NOT NULL,
    regdate character varying(64) NOT NULL,
    chaircnt integer NOT NULL,
    projcnt integer NOT NULL,
    boardcnt integer NOT NULL,
    regtimestart integer NOT NULL,
    regtimestop integer NOT NULL
);
 "   DROP TABLE public.requests_table;
       public         heap    postgres    false         ?            1259    16404    resource_table    TABLE     ?   CREATE TABLE public.resource_table (
    type character varying(32) NOT NULL,
    name character varying(64) NOT NULL,
    count integer NOT NULL
);
 "   DROP TABLE public.resource_table;
       public         heap    postgres    false         ?            1259    16399    users_table    TABLE     ?   CREATE TABLE public.users_table (
    userid character varying(64) NOT NULL,
    login character varying(32) NOT NULL,
    password character varying(32) NOT NULL,
    type character varying(32) NOT NULL,
    fio character varying(256) NOT NULL
);
    DROP TABLE public.users_table;
       public         heap    postgres    false                   0    16426 	   msg_table 
   TABLE DATA                 public          postgres    false    216       3339.dat           0    16443    requests_table 
   TABLE DATA                 public          postgres    false    217       3340.dat 
          0    16404    resource_table 
   TABLE DATA                 public          postgres    false    215       3338.dat 	          0    16399    users_table 
   TABLE DATA                 public          postgres    false    214       3337.dat u           2606    16432    msg_table msg_table_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY public.msg_table
    ADD CONSTRAINT msg_table_pkey PRIMARY KEY (msgid);
 B   ALTER TABLE ONLY public.msg_table DROP CONSTRAINT msg_table_pkey;
       public            postgres    false    216         w           2606    16447 "   requests_table requests_table_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.requests_table
    ADD CONSTRAINT requests_table_pkey PRIMARY KEY (reqid);
 L   ALTER TABLE ONLY public.requests_table DROP CONSTRAINT requests_table_pkey;
       public            postgres    false    217         s           2606    16408 "   resource_table resourse_table_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.resource_table
    ADD CONSTRAINT resourse_table_pkey PRIMARY KEY (type);
 L   ALTER TABLE ONLY public.resource_table DROP CONSTRAINT resourse_table_pkey;
       public            postgres    false    215         q           2606    16403    users_table users_table_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.users_table
    ADD CONSTRAINT users_table_pkey PRIMARY KEY (userid);
 F   ALTER TABLE ONLY public.users_table DROP CONSTRAINT users_table_pkey;
       public            postgres    false    214         z           2606    16448    requests_table userid    FK CONSTRAINT     ?   ALTER TABLE ONLY public.requests_table
    ADD CONSTRAINT userid FOREIGN KEY (userid) REFERENCES public.users_table(userid) ON UPDATE CASCADE ON DELETE CASCADE;
 ?   ALTER TABLE ONLY public.requests_table DROP CONSTRAINT userid;
       public          postgres    false    217    214    3185         x           2606    16433    msg_table userkeyfrom    FK CONSTRAINT     ?   ALTER TABLE ONLY public.msg_table
    ADD CONSTRAINT userkeyfrom FOREIGN KEY (useridfrom) REFERENCES public.users_table(userid) ON UPDATE CASCADE ON DELETE CASCADE;
 ?   ALTER TABLE ONLY public.msg_table DROP CONSTRAINT userkeyfrom;
       public          postgres    false    3185    216    214         y           2606    16438    msg_table userkeyto    FK CONSTRAINT     ?   ALTER TABLE ONLY public.msg_table
    ADD CONSTRAINT userkeyto FOREIGN KEY (useridto) REFERENCES public.users_table(userid) ON UPDATE CASCADE ON DELETE CASCADE;
 =   ALTER TABLE ONLY public.msg_table DROP CONSTRAINT userkeyto;
       public          postgres    false    3185    216    214                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           3339.dat                                                                                            0000600 0004000 0002000 00000000002 14343605161 0014246 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              3340.dat                                                                                            0000600 0004000 0002000 00000000002 14343605161 0014236 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              3338.dat                                                                                            0000600 0004000 0002000 00000000603 14343605161 0014254 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        INSERT INTO public.resource_table (type, name, count) VALUES ('CHAIRS', 'Стулья', 0);
INSERT INTO public.resource_table (type, name, count) VALUES ('BOARDS', 'Доски', 0);
INSERT INTO public.resource_table (type, name, count) VALUES ('AUDIENCES', 'Аудитории', 0);
INSERT INTO public.resource_table (type, name, count) VALUES ('PROJECTORS', 'Проекторы', 0);


                                                                                                                             3337.dat                                                                                            0000600 0004000 0002000 00000000002 14343605161 0014244 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              restore.sql                                                                                         0000600 0004000 0002000 00000011505 14343605161 0015371 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        --
-- NOTE:
--
-- File paths need to be edited. Search for $$PATH$$ and
-- replace it with the path to the directory containing
-- the extracted data files.
--
--
-- PostgreSQL database dump
--

-- Dumped from database version 15.1
-- Dumped by pg_dump version 15.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE piaps;
--
-- Name: piaps; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE piaps WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';


ALTER DATABASE piaps OWNER TO postgres;

\connect piaps

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: msg_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.msg_table (
    msgid character varying(64) NOT NULL,
    useridfrom character varying(64) NOT NULL,
    useridto character varying(64) NOT NULL,
    strtimestamp character varying(64) NOT NULL,
    theme character varying(256) NOT NULL,
    body character varying(1024) NOT NULL
);


ALTER TABLE public.msg_table OWNER TO postgres;

--
-- Name: requests_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.requests_table (
    reqid character varying(64) NOT NULL,
    userid character varying(64) NOT NULL,
    status character varying(32) NOT NULL,
    adtnum integer NOT NULL,
    regdate character varying(64) NOT NULL,
    chaircnt integer NOT NULL,
    projcnt integer NOT NULL,
    boardcnt integer NOT NULL,
    regtimestart integer NOT NULL,
    regtimestop integer NOT NULL
);


ALTER TABLE public.requests_table OWNER TO postgres;

--
-- Name: resource_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.resource_table (
    type character varying(32) NOT NULL,
    name character varying(64) NOT NULL,
    count integer NOT NULL
);


ALTER TABLE public.resource_table OWNER TO postgres;

--
-- Name: users_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_table (
    userid character varying(64) NOT NULL,
    login character varying(32) NOT NULL,
    password character varying(32) NOT NULL,
    type character varying(32) NOT NULL,
    fio character varying(256) NOT NULL
);


ALTER TABLE public.users_table OWNER TO postgres;

--
-- Data for Name: msg_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

\i $$PATH$$/3339.dat

--
-- Data for Name: requests_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

\i $$PATH$$/3340.dat

--
-- Data for Name: resource_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

\i $$PATH$$/3338.dat

--
-- Data for Name: users_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

\i $$PATH$$/3337.dat

--
-- Name: msg_table msg_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.msg_table
    ADD CONSTRAINT msg_table_pkey PRIMARY KEY (msgid);


--
-- Name: requests_table requests_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.requests_table
    ADD CONSTRAINT requests_table_pkey PRIMARY KEY (reqid);


--
-- Name: resource_table resourse_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resource_table
    ADD CONSTRAINT resourse_table_pkey PRIMARY KEY (type);


--
-- Name: users_table users_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_table
    ADD CONSTRAINT users_table_pkey PRIMARY KEY (userid);


--
-- Name: requests_table userid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.requests_table
    ADD CONSTRAINT userid FOREIGN KEY (userid) REFERENCES public.users_table(userid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: msg_table userkeyfrom; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.msg_table
    ADD CONSTRAINT userkeyfrom FOREIGN KEY (useridfrom) REFERENCES public.users_table(userid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: msg_table userkeyto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.msg_table
    ADD CONSTRAINT userkeyto FOREIGN KEY (useridto) REFERENCES public.users_table(userid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           