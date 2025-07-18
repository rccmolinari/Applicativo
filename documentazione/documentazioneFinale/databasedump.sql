PGDMP  -                    }        
   aereoporto    17.5    17.5 2    ]           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            ^           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            _           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            `           1262    17986 
   aereoporto    DATABASE     }   CREATE DATABASE aereoporto WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE aereoporto;
                     postgres    false            �            1255    18100    check_consistenza_stato_volo()    FUNCTION     $  CREATE FUNCTION public.check_consistenza_stato_volo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Se il volo è CANCELLATO, non fare altri controlli
    IF NEW.stato_volo = 'CANCELLATO' THEN
        IF NEW.data_volo < CURRENT_DATE THEN
            RAISE EXCEPTION 'Non puoi cancellare un volo già passato.';
        END IF;
        RETURN NEW;
    END IF;

    -- Se ha ritardo > 0, forza lo stato a 'RITARDO'
    IF NEW.ritardo > 0 THEN
        NEW.stato_volo := 'RITARDO';

    -- Se ritardo è 0 e data volo è nel passato, imposta 'DECOLLATO'
    ELSIF NEW.ritardo = 0 AND NEW.data_volo < CURRENT_DATE THEN
        NEW.stato_volo := 'DECOLLATO';

    -- Altrimenti, mantieni stato 'PROGRAMMATO'
    ELSE
        NEW.stato_volo := 'PROGRAMMATO';
    END IF;

    RETURN NEW;
END;
$$;
 5   DROP FUNCTION public.check_consistenza_stato_volo();
       public               postgres    false            �            1259    17989    amministratore    TABLE     �   CREATE TABLE public.amministratore (
    username character varying(20) NOT NULL,
    password character varying(20) NOT NULL
);
 "   DROP TABLE public.amministratore;
       public         heap r       postgres    false            �            1259    18017    bagaglio    TABLE     �   CREATE TABLE public.bagaglio (
    stato_bagaglio character varying(20) NOT NULL,
    id_bagaglio integer NOT NULL,
    numero_prenotazione integer
);
    DROP TABLE public.bagaglio;
       public         heap r       postgres    false            �            1259    17987    bagaglio_id_bagaglio_seq    SEQUENCE     �   CREATE SEQUENCE public.bagaglio_id_bagaglio_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.bagaglio_id_bagaglio_seq;
       public               postgres    false            �            1259    18070    bagaglio_id_bagaglio_seq1    SEQUENCE     �   CREATE SEQUENCE public.bagaglio_id_bagaglio_seq1
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.bagaglio_id_bagaglio_seq1;
       public               postgres    false    224            a           0    0    bagaglio_id_bagaglio_seq1    SEQUENCE OWNED BY     V   ALTER SEQUENCE public.bagaglio_id_bagaglio_seq1 OWNED BY public.bagaglio.id_bagaglio;
          public               postgres    false    227            �            1259    18066    bagaglio_id_seq    SEQUENCE     x   CREATE SEQUENCE public.bagaglio_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.bagaglio_id_seq;
       public               postgres    false            �            1259    17999 
   passeggero    TABLE     �   CREATE TABLE public.passeggero (
    id_documento character varying(10) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    data_nascita date NOT NULL
);
    DROP TABLE public.passeggero;
       public         heap r       postgres    false            �            1259    18010    prenotazione    TABLE     6  CREATE TABLE public.prenotazione (
    username character varying(50),
    password character varying(50),
    documento_passeggero character varying(10),
    posto_assegnato character varying(10),
    stato_prenotazione character varying(20),
    numero_biglietto integer NOT NULL,
    codice_volo integer
);
     DROP TABLE public.prenotazione;
       public         heap r       postgres    false            �            1259    17988     prenotazione_id_prenotazione_seq    SEQUENCE     �   CREATE SEQUENCE public.prenotazione_id_prenotazione_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 7   DROP SEQUENCE public.prenotazione_id_prenotazione_seq;
       public               postgres    false            �            1259    18078 !   prenotazione_numero_biglietto_seq    SEQUENCE     �   CREATE SEQUENCE public.prenotazione_numero_biglietto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.prenotazione_numero_biglietto_seq;
       public               postgres    false            �            1259    18082 "   prenotazione_numero_biglietto_seq1    SEQUENCE     �   CREATE SEQUENCE public.prenotazione_numero_biglietto_seq1
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 9   DROP SEQUENCE public.prenotazione_numero_biglietto_seq1;
       public               postgres    false    223            b           0    0 "   prenotazione_numero_biglietto_seq1    SEQUENCE OWNED BY     h   ALTER SEQUENCE public.prenotazione_numero_biglietto_seq1 OWNED BY public.prenotazione.numero_biglietto;
          public               postgres    false    229            �            1259    17994    utente_generico    TABLE     �   CREATE TABLE public.utente_generico (
    username character varying(20) NOT NULL,
    password character varying(20) NOT NULL
);
 #   DROP TABLE public.utente_generico;
       public         heap r       postgres    false            �            1259    18024    voligestiti    TABLE     �   CREATE TABLE public.voligestiti (
    username_amministratore character varying(20) NOT NULL,
    password_amministratore character varying(20) NOT NULL,
    codice_volo integer NOT NULL
);
    DROP TABLE public.voligestiti;
       public         heap r       postgres    false            �            1259    18004    volo    TABLE     �  CREATE TABLE public.volo (
    codice_volo integer NOT NULL,
    compagnia character varying(20),
    data_volo date NOT NULL,
    orario_previsto time without time zone NOT NULL,
    ritardo integer,
    stato_volo character varying(20),
    tipo_volo character varying(20) NOT NULL,
    aeroporto_destinazione character varying(20),
    aeroporto_origine character varying(20),
    gate character varying(10)
);
    DROP TABLE public.volo;
       public         heap r       postgres    false            �           2604    18071    bagaglio id_bagaglio    DEFAULT     }   ALTER TABLE ONLY public.bagaglio ALTER COLUMN id_bagaglio SET DEFAULT nextval('public.bagaglio_id_bagaglio_seq1'::regclass);
 C   ALTER TABLE public.bagaglio ALTER COLUMN id_bagaglio DROP DEFAULT;
       public               postgres    false    227    224            �           2604    18083    prenotazione numero_biglietto    DEFAULT     �   ALTER TABLE ONLY public.prenotazione ALTER COLUMN numero_biglietto SET DEFAULT nextval('public.prenotazione_numero_biglietto_seq1'::regclass);
 L   ALTER TABLE public.prenotazione ALTER COLUMN numero_biglietto DROP DEFAULT;
       public               postgres    false    229    223            P          0    17989    amministratore 
   TABLE DATA           <   COPY public.amministratore (username, password) FROM stdin;
    public               postgres    false    219   �B       U          0    18017    bagaglio 
   TABLE DATA           T   COPY public.bagaglio (stato_bagaglio, id_bagaglio, numero_prenotazione) FROM stdin;
    public               postgres    false    224   C       R          0    17999 
   passeggero 
   TABLE DATA           O   COPY public.passeggero (id_documento, nome, cognome, data_nascita) FROM stdin;
    public               postgres    false    221   �C       T          0    18010    prenotazione 
   TABLE DATA           �   COPY public.prenotazione (username, password, documento_passeggero, posto_assegnato, stato_prenotazione, numero_biglietto, codice_volo) FROM stdin;
    public               postgres    false    223   MD       Q          0    17994    utente_generico 
   TABLE DATA           =   COPY public.utente_generico (username, password) FROM stdin;
    public               postgres    false    220   �D       V          0    18024    voligestiti 
   TABLE DATA           d   COPY public.voligestiti (username_amministratore, password_amministratore, codice_volo) FROM stdin;
    public               postgres    false    225   )E       S          0    18004    volo 
   TABLE DATA           �   COPY public.volo (codice_volo, compagnia, data_volo, orario_previsto, ritardo, stato_volo, tipo_volo, aeroporto_destinazione, aeroporto_origine, gate) FROM stdin;
    public               postgres    false    222   FE       c           0    0    bagaglio_id_bagaglio_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.bagaglio_id_bagaglio_seq', 1, false);
          public               postgres    false    217            d           0    0    bagaglio_id_bagaglio_seq1    SEQUENCE SET     I   SELECT pg_catalog.setval('public.bagaglio_id_bagaglio_seq1', 203, true);
          public               postgres    false    227            e           0    0    bagaglio_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.bagaglio_id_seq', 1, false);
          public               postgres    false    226            f           0    0     prenotazione_id_prenotazione_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.prenotazione_id_prenotazione_seq', 1, false);
          public               postgres    false    218            g           0    0 !   prenotazione_numero_biglietto_seq    SEQUENCE SET     P   SELECT pg_catalog.setval('public.prenotazione_numero_biglietto_seq', 1, false);
          public               postgres    false    228            h           0    0 "   prenotazione_numero_biglietto_seq1    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.prenotazione_numero_biglietto_seq1', 34, true);
          public               postgres    false    229            �           2606    17993 "   amministratore amministratore_pkey 
   CONSTRAINT     p   ALTER TABLE ONLY public.amministratore
    ADD CONSTRAINT amministratore_pkey PRIMARY KEY (username, password);
 L   ALTER TABLE ONLY public.amministratore DROP CONSTRAINT amministratore_pkey;
       public                 postgres    false    219    219            �           2606    18077    bagaglio bagaglio_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.bagaglio
    ADD CONSTRAINT bagaglio_pkey PRIMARY KEY (id_bagaglio);
 @   ALTER TABLE ONLY public.bagaglio DROP CONSTRAINT bagaglio_pkey;
       public                 postgres    false    224            �           2606    18003    passeggero passeggero_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.passeggero
    ADD CONSTRAINT passeggero_pkey PRIMARY KEY (id_documento);
 D   ALTER TABLE ONLY public.passeggero DROP CONSTRAINT passeggero_pkey;
       public                 postgres    false    221            �           2606    18085    prenotazione prenotazione_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT prenotazione_pkey PRIMARY KEY (numero_biglietto);
 H   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT prenotazione_pkey;
       public                 postgres    false    223            �           2606    18335     prenotazione unique_pg_documento 
   CONSTRAINT     x   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT unique_pg_documento UNIQUE (codice_volo, documento_passeggero);
 J   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT unique_pg_documento;
       public                 postgres    false    223    223            �           2606    17998 $   utente_generico utente_generico_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.utente_generico
    ADD CONSTRAINT utente_generico_pkey PRIMARY KEY (username, password);
 N   ALTER TABLE ONLY public.utente_generico DROP CONSTRAINT utente_generico_pkey;
       public                 postgres    false    220    220            �           2606    18028    voligestiti voligestiti_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.voligestiti
    ADD CONSTRAINT voligestiti_pkey PRIMARY KEY (username_amministratore, password_amministratore, codice_volo);
 F   ALTER TABLE ONLY public.voligestiti DROP CONSTRAINT voligestiti_pkey;
       public                 postgres    false    225    225    225            �           2606    18009    volo volo_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.volo
    ADD CONSTRAINT volo_pkey PRIMARY KEY (codice_volo);
 8   ALTER TABLE ONLY public.volo DROP CONSTRAINT volo_pkey;
       public                 postgres    false    222            �           2620    18101 #   volo trigger_consistenza_stato_volo    TRIGGER     �   CREATE TRIGGER trigger_consistenza_stato_volo BEFORE INSERT OR UPDATE ON public.volo FOR EACH ROW EXECUTE FUNCTION public.check_consistenza_stato_volo();
 <   DROP TRIGGER trigger_consistenza_stato_volo ON public.volo;
       public               postgres    false    230    222            �           2606    18313 !   bagaglio fk_bagaglio_prenotazione    FK CONSTRAINT     �   ALTER TABLE ONLY public.bagaglio
    ADD CONSTRAINT fk_bagaglio_prenotazione FOREIGN KEY (numero_prenotazione) REFERENCES public.prenotazione(numero_biglietto) ON UPDATE CASCADE ON DELETE CASCADE;
 K   ALTER TABLE ONLY public.bagaglio DROP CONSTRAINT fk_bagaglio_prenotazione;
       public               postgres    false    224    223    4783            �           2606    18298    prenotazione fk_codice_volo    FK CONSTRAINT     �   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT fk_codice_volo FOREIGN KEY (codice_volo) REFERENCES public.volo(codice_volo) ON UPDATE CASCADE ON DELETE CASCADE;
 E   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT fk_codice_volo;
       public               postgres    false    223    222    4781            �           2606    18288 &   voligestiti fk_gestione_amministratore    FK CONSTRAINT     �   ALTER TABLE ONLY public.voligestiti
    ADD CONSTRAINT fk_gestione_amministratore FOREIGN KEY (username_amministratore, password_amministratore) REFERENCES public.amministratore(username, password) ON UPDATE CASCADE ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.voligestiti DROP CONSTRAINT fk_gestione_amministratore;
       public               postgres    false    225    219    219    225    4775            �           2606    18293    voligestiti fk_gestione_volo    FK CONSTRAINT     �   ALTER TABLE ONLY public.voligestiti
    ADD CONSTRAINT fk_gestione_volo FOREIGN KEY (codice_volo) REFERENCES public.volo(codice_volo) ON UPDATE CASCADE ON DELETE CASCADE;
 F   ALTER TABLE ONLY public.voligestiti DROP CONSTRAINT fk_gestione_volo;
       public               postgres    false    222    225    4781            �           2606    18303 '   prenotazione fk_prenotazione_passeggero    FK CONSTRAINT     �   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT fk_prenotazione_passeggero FOREIGN KEY (documento_passeggero) REFERENCES public.passeggero(id_documento) ON UPDATE CASCADE ON DELETE CASCADE;
 Q   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT fk_prenotazione_passeggero;
       public               postgres    false    221    223    4779            �           2606    18308 #   prenotazione fk_prenotazione_utente    FK CONSTRAINT     �   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT fk_prenotazione_utente FOREIGN KEY (username, password) REFERENCES public.utente_generico(username, password) ON UPDATE CASCADE ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT fk_prenotazione_utente;
       public               postgres    false    223    220    220    223    4777            P      x�KL����L�\1z\\\ 4�      U   p   x�e�;
�0��zs�G4�]l,D,DXs$b����f|����RO�̤9�WDA$�� H�d�K�K�&ױ��O3,�Lz1�Hf�� H"��d��ek�R���B��R�P      R   �   x����0����ق�=�h���xiH��X����-�y3/��s!X~�e�Y�eBJ	x��Q�V��5&,γn��l�89 `I�s���&���q����4�^շB*����e�3@yUt�Wv������D_"���e{�v~��r��0���s���1�      T   �   x�]α
�0����a$7I�S�P�Ҋ.���p�$�>�-:4���t#
@a��=n
����#��Qr^0Z���C�!�b-%_d��Z[{F9�/�>�7<\J�G8�v0���� T(Կޙ��c�̰\��,�m��\�3�~��c�LeK6      Q   (   x�+J-ί�,H,..�/J���������OI������ ��
�      V      x������ � �      S     x�}�Mn�0���\������w&A�-1(�"6Yd	A��V����J�� ���BL�؍��t=0r"0��fԈ@9�vo�r���}�ygv�<�pv!�p������9
�+ �9�q�<���+����PJ 	f���iH������T��c����:6�$�
����ۥ�=.ݵB�L`.�S��ٿT]���!��7��<��Q�L�dv�U��lf�O�ri�U|�U3<^���� * i�q��	�ދ����&o֭�ᛐ �4_��2d��%��o��s�     