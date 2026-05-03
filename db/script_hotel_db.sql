--
-- PostgreSQL database dump
--

\restrict 2kdbcJUMxhk9MQTLdPTgMYvBV3knVgWHPwrdCaOvXGz0DgQjLF3AzRP13jxcGj9

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-05-02 12:20:16

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 236 (class 1255 OID 41737)
-- Name: ajustar_precios_temporada(numeric); Type: PROCEDURE; Schema: public; Owner: -
--

CREATE PROCEDURE public.ajustar_precios_temporada(IN porcentaje_cambio numeric)
    LANGUAGE plpgsql
    AS $$
DECLARE
    fila_tipo RECORD;
BEGIN
    -- Uso de LOOP para recorrer y validar (Estructura de control solicitada)
    FOR fila_tipo IN SELECT id_tipo, nombre_tipo, precio_noche
	FROM tipo_habitacion LOOP
        IF porcentaje_cambio > 0 THEN
            UPDATE tipo_habitacion 
            SET precio_noche = precio_noche * (1 + porcentaje_cambio)
            WHERE id_tipo = fila_tipo.id_tipo;
        END IF;
    END LOOP;
    
    RAISE NOTICE 'Ajuste masivo de precios completado.';
EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Error al realizar la actualización masiva: %', SQLERRM;
END;
$$;


--
-- TOC entry 231 (class 1255 OID 33546)
-- Name: crear_reserva(integer, integer, date, date); Type: PROCEDURE; Schema: public; Owner: -
--

CREATE PROCEDURE public.crear_reserva(IN p_cliente integer, IN p_habitacion integer, IN p_inicio date, IN p_fin date)
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Llama a la función 'validar_cliente' para asegurar que el 
	--ID existe en la tabla.
    IF NOT validar_cliente(p_cliente) THEN
        -- Si no existe, se lanza una excepción que cancela toda la operación (Rollback).
        RAISE EXCEPTION 'Cliente no registrado';
    END IF;
    -- Llama a 'validar_disponibilidad' para chequear traslapes con otras reservas.
    IF NOT validar_disponibilidad(p_habitacion, p_inicio, p_fin) THEN
        -- Si la habitación está ocupada en ese rango, se detiene el proceso.
        RAISE EXCEPTION 'Habitación no disponible';
    END IF;
    -- Una vez superadas las validaciones, se inserta el nuevo registro.
    -- Se establece el estado como 'confirmada' según tu definición.
    INSERT INTO reservas(id_cliente, id_habitacion, fecha_inicio, fecha_fin, estado)
    VALUES (p_cliente, p_habitacion, p_inicio, p_fin, 'confirmada');
    /* Nota: si alguna de las excepciones anteriores se dispara, 
       los cambios no se guardan, manteniendo la integridad de la base de datos.*/
END;$$;


--
-- TOC entry 235 (class 1255 OID 41736)
-- Name: obtener_historial_cliente(character varying); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.obtener_historial_cliente(p_documento character varying) RETURNS TABLE(reserva_id integer, habitacion character varying, entrada date, salida date, estado_reserva character varying, dias_estadia integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT r.id_reserva, h.numero_habitacion, r.fecha_entrada, r.fecha_salida, r.estado,
           (r.fecha_salida - r.fecha_entrada) AS dias
    FROM reserva r
    JOIN cliente c ON r.id_cliente = c.id_cliente
    JOIN habitacion h ON r.id_habitacion = h.id_habitacion
    WHERE c.documento = p_documento;
END;
$$;


--
-- TOC entry 233 (class 1255 OID 33548)
-- Name: registrar_checkin(integer); Type: PROCEDURE; Schema: public; Owner: -
--

CREATE PROCEDURE public.registrar_checkin(IN p_reserva integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
	/*validación para asegurar que la reserva exista 
	antes de intentar actualizarla*/
	IF NOT EXISTS (SELECT 1 FROM reserva WHERE id_reserva = p_reserva) THEN
		RAISE EXCEPTION 'No se encontró la reserva';	
	END IF;
    /* después de confirmar que la reserva existe,
	   Se actualiza el estado de la reserva a 'confirmada'.
       Esto indica que el cliente ha llegado y se ha formalizado su ingreso.*/
    UPDATE reserva
    SET estado = 'confirmada'
    WHERE id_reserva = p_reserva;
END;
$$;


--
-- TOC entry 234 (class 1255 OID 33549)
-- Name: registrar_pago(numeric, character varying, integer); Type: PROCEDURE; Schema: public; Owner: -
--

CREATE PROCEDURE public.registrar_pago(IN p_monto numeric, IN p_metodopago character varying, IN p_reserva integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO pagos(monto, metodo_pago, fecha_pago, id_reserva)
    VALUES (p_monto, p_metodoPago, CURRENT_DATE, p_reserva);
END;
$$;


--
-- TOC entry 232 (class 1255 OID 33547)
-- Name: total_pagado_reserva(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.total_pagado_reserva(p_reserva integer) RETURNS numeric
    LANGUAGE plpgsql
    AS $$
DECLARE
    total NUMERIC;
BEGIN
    SELECT COALESCE(SUM(monto),0)
    INTO total
    FROM pagos
    WHERE id_reserva = p_reserva;

    RETURN total;
END;
$$;


--
-- TOC entry 230 (class 1255 OID 33545)
-- Name: validar_cliente(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.validar_cliente(p_cedula integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
	existe VARCHAR(11);
BEGIN
	select count(*) INTO existe
	from cliente
	where documento = p_cedula;

	IF existe > 0 THEN 
		return true;
	ELSE
		return false;
	END IF;
END;
$$;


--
-- TOC entry 229 (class 1255 OID 33544)
-- Name: validar_disponibilidad(integer, date, date); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.validar_disponibilidad(p_habitacion integer, p_inicio date, p_fin date) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    existe INT;
BEGIN
    SELECT COUNT(*) INTO existe
    FROM reservas
    WHERE id_habitacion = p_habitacion
    AND (estado = 'confirmada' OR estado = 'pendiente')
    AND (p_inicio BETWEEN fecha_inicio AND fecha_fin
         OR p_fin BETWEEN fecha_inicio AND fecha_fin);

    IF existe > 0 THEN
        RETURN FALSE;
    ELSE
        RETURN TRUE;
    END IF;
END;
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 25842)
-- Name: cliente; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cliente (
    id_cliente integer NOT NULL,
    nombre character varying(50) NOT NULL,
    apellido character varying(50) NOT NULL,
    telefono character varying(12) NOT NULL,
    email character varying(100) NOT NULL,
    documento character varying(11) NOT NULL,
    fecha_registro date DEFAULT CURRENT_DATE
);


--
-- TOC entry 220 (class 1259 OID 25852)
-- Name: cliente_id_cliente_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cliente_id_cliente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5082 (class 0 OID 0)
-- Dependencies: 220
-- Name: cliente_id_cliente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cliente_id_cliente_seq OWNED BY public.cliente.id_cliente;


--
-- TOC entry 221 (class 1259 OID 25853)
-- Name: habitacion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.habitacion (
    id_habitacion integer NOT NULL,
    numero_habitacion character varying(6) NOT NULL,
    disponible boolean NOT NULL,
    id_tipo integer NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 25860)
-- Name: habitacion_id_habitacion_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.habitacion_id_habitacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5083 (class 0 OID 0)
-- Dependencies: 222
-- Name: habitacion_id_habitacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.habitacion_id_habitacion_seq OWNED BY public.habitacion.id_habitacion;


--
-- TOC entry 223 (class 1259 OID 25861)
-- Name: pago; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pago (
    id_pago integer NOT NULL,
    monto numeric(10,2),
    metodo_pago character varying(15) NOT NULL,
    fecha_pago date DEFAULT CURRENT_DATE,
    id_reserva integer NOT NULL,
    CONSTRAINT pago_metodo_pago_check CHECK (((metodo_pago)::text = ANY (ARRAY[('Efectivo'::character varying)::text, ('Tarjeta'::character varying)::text, ('Transferencia'::character varying)::text]))),
    CONSTRAINT pago_monto_check CHECK ((monto > (0)::numeric))
);


--
-- TOC entry 224 (class 1259 OID 25870)
-- Name: pago_id_pago_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pago_id_pago_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5084 (class 0 OID 0)
-- Dependencies: 224
-- Name: pago_id_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pago_id_pago_seq OWNED BY public.pago.id_pago;


--
-- TOC entry 225 (class 1259 OID 25871)
-- Name: reserva; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.reserva (
    id_reserva integer NOT NULL,
    fecha_reserva date DEFAULT CURRENT_DATE NOT NULL,
    fecha_entrada date NOT NULL,
    fecha_salida date NOT NULL,
    estado character varying(15) DEFAULT 'Pendiente'::character varying,
    id_cliente integer NOT NULL,
    id_habitacion integer NOT NULL,
    CONSTRAINT reserva_check CHECK ((fecha_entrada >= fecha_reserva)),
    CONSTRAINT reserva_check1 CHECK ((fecha_salida >= fecha_entrada)),
    CONSTRAINT reserva_estado_check CHECK (((estado)::text = ANY (ARRAY[('Pendiente'::character varying)::text, ('Confirmada'::character varying)::text, ('Cancelada'::character varying)::text])))
);


--
-- TOC entry 226 (class 1259 OID 25885)
-- Name: reserva_id_reserva_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reserva_id_reserva_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5085 (class 0 OID 0)
-- Dependencies: 226
-- Name: reserva_id_reserva_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reserva_id_reserva_seq OWNED BY public.reserva.id_reserva;


--
-- TOC entry 227 (class 1259 OID 25886)
-- Name: tipo_habitacion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_habitacion (
    id_tipo integer NOT NULL,
    nombre_tipo character varying(50) NOT NULL,
    precio_noche numeric(10,2) NOT NULL,
    capacidad integer NOT NULL,
    CONSTRAINT tipo_habitacion_capacidad_check CHECK ((capacidad > 0)),
    CONSTRAINT tipo_habitacion_precio_noche_check CHECK ((precio_noche > (0)::numeric))
);


--
-- TOC entry 228 (class 1259 OID 25895)
-- Name: tipo_habitacion_id_tipo_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tipo_habitacion_id_tipo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5086 (class 0 OID 0)
-- Dependencies: 228
-- Name: tipo_habitacion_id_tipo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tipo_habitacion_id_tipo_seq OWNED BY public.tipo_habitacion.id_tipo;


--
-- TOC entry 4886 (class 2604 OID 25896)
-- Name: cliente id_cliente; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cliente ALTER COLUMN id_cliente SET DEFAULT nextval('public.cliente_id_cliente_seq'::regclass);


--
-- TOC entry 4888 (class 2604 OID 25897)
-- Name: habitacion id_habitacion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.habitacion ALTER COLUMN id_habitacion SET DEFAULT nextval('public.habitacion_id_habitacion_seq'::regclass);


--
-- TOC entry 4889 (class 2604 OID 25898)
-- Name: pago id_pago; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago ALTER COLUMN id_pago SET DEFAULT nextval('public.pago_id_pago_seq'::regclass);


--
-- TOC entry 4891 (class 2604 OID 25899)
-- Name: reserva id_reserva; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reserva ALTER COLUMN id_reserva SET DEFAULT nextval('public.reserva_id_reserva_seq'::regclass);


--
-- TOC entry 4894 (class 2604 OID 25900)
-- Name: tipo_habitacion id_tipo; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_habitacion ALTER COLUMN id_tipo SET DEFAULT nextval('public.tipo_habitacion_id_tipo_seq'::regclass);


--
-- TOC entry 5067 (class 0 OID 25842)
-- Dependencies: 219
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.cliente (id_cliente, nombre, apellido, telefono, email, documento, fecha_registro) FROM stdin;
2	Maria	Gomez	3012345678	maria.gomez@email.com	10023456789	2026-03-14
3	Carlos	Rodriguez	3023456789	carlos.rodriguez@email.com	10034567890	2026-03-14
5	Andres	Lopez	3045678901	andres.lopez@email.com	10056789012	2026-03-14
6	Sofia	Ramirez	3056789012	sofia.ramirez@email.com	10067890123	2026-03-14
7	Diego	Torres	3067890123	diego.torres@email.com	10078901234	2026-03-14
8	Valentina	Castro	3078901234	valentina.castro@email.com	10089012345	2026-03-14
9	Daniel	Vargas	3089012345	daniel.vargas@email.com	10090123456	2026-03-14
10	Camila	Rojas	3090123456	camila.rojas@email.com	10101234567	2026-03-14
1	Juan	Perez	3001234567	juan.perez@email.com	10012345678	2026-03-14
4	Angie	Diaz	3034567890	laura.martinez@email.com	10045678901	2026-03-14
\.


--
-- TOC entry 5069 (class 0 OID 25853)
-- Dependencies: 221
-- Data for Name: habitacion; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.habitacion (id_habitacion, numero_habitacion, disponible, id_tipo) FROM stdin;
1	101	t	1
2	102	t	2
3	103	t	3
4	104	t	4
5	105	t	5
6	201	t	6
7	202	t	7
8	203	t	8
9	204	t	9
10	205	t	10
\.


--
-- TOC entry 5071 (class 0 OID 25861)
-- Dependencies: 223
-- Data for Name: pago; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.pago (id_pago, monto, metodo_pago, fecha_pago, id_reserva) FROM stdin;
1	50000.00	Tarjeta	2026-05-02	7
\.


--
-- TOC entry 5073 (class 0 OID 25871)
-- Dependencies: 225
-- Data for Name: reserva; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.reserva (id_reserva, fecha_reserva, fecha_entrada, fecha_salida, estado, id_cliente, id_habitacion) FROM stdin;
2	2026-04-20	2026-04-21	2026-04-25	Confirmada	1	1
3	2026-04-20	2026-04-22	2026-04-26	Pendiente	2	2
4	2026-04-21	2026-04-23	2026-04-27	Confirmada	3	3
5	2026-04-21	2026-04-24	2026-04-28	Cancelada	4	4
6	2026-04-22	2026-04-25	2026-04-29	Confirmada	5	5
7	2026-04-22	2026-04-26	2026-04-30	Pendiente	6	6
8	2026-04-23	2026-05-01	2026-05-05	Confirmada	7	7
9	2026-04-23	2026-05-02	2026-05-06	Confirmada	8	8
10	2026-04-24	2026-05-03	2026-05-07	Pendiente	9	9
11	2026-04-24	2026-05-04	2026-05-08	Confirmada	10	10
\.


--
-- TOC entry 5075 (class 0 OID 25886)
-- Dependencies: 227
-- Data for Name: tipo_habitacion; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tipo_habitacion (id_tipo, nombre_tipo, precio_noche, capacidad) FROM stdin;
1	Simple	80.00	1
2	Doble	120.00	2
3	Triple	150.00	3
4	Suite Junior	200.00	2
5	Suite	300.00	4
6	Familiar	250.00	5
7	Ejecutiva	180.00	2
8	Premium	350.00	3
9	Deluxe	400.00	4
10	Presidencial	600.00	6
\.


--
-- TOC entry 5087 (class 0 OID 0)
-- Dependencies: 220
-- Name: cliente_id_cliente_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.cliente_id_cliente_seq', 11, true);


--
-- TOC entry 5088 (class 0 OID 0)
-- Dependencies: 222
-- Name: habitacion_id_habitacion_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.habitacion_id_habitacion_seq', 10, true);


--
-- TOC entry 5089 (class 0 OID 0)
-- Dependencies: 224
-- Name: pago_id_pago_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.pago_id_pago_seq', 1, true);


--
-- TOC entry 5090 (class 0 OID 0)
-- Dependencies: 226
-- Name: reserva_id_reserva_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.reserva_id_reserva_seq', 11, true);


--
-- TOC entry 5091 (class 0 OID 0)
-- Dependencies: 228
-- Name: tipo_habitacion_id_tipo_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tipo_habitacion_id_tipo_seq', 10, true);


--
-- TOC entry 4903 (class 2606 OID 25902)
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- TOC entry 4907 (class 2606 OID 25904)
-- Name: habitacion habitacion_numero_habitacion_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.habitacion
    ADD CONSTRAINT habitacion_numero_habitacion_key UNIQUE (numero_habitacion);


--
-- TOC entry 4909 (class 2606 OID 25906)
-- Name: habitacion habitacion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.habitacion
    ADD CONSTRAINT habitacion_pkey PRIMARY KEY (id_habitacion);


--
-- TOC entry 4911 (class 2606 OID 25908)
-- Name: pago pago_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago
    ADD CONSTRAINT pago_pkey PRIMARY KEY (id_pago);


--
-- TOC entry 4913 (class 2606 OID 25910)
-- Name: reserva reserva_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT reserva_pkey PRIMARY KEY (id_reserva);


--
-- TOC entry 4915 (class 2606 OID 25912)
-- Name: tipo_habitacion tipo_habitacion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_habitacion
    ADD CONSTRAINT tipo_habitacion_pkey PRIMARY KEY (id_tipo);


--
-- TOC entry 4905 (class 2606 OID 25914)
-- Name: cliente unique_documento_cliente; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT unique_documento_cliente UNIQUE (documento);


--
-- TOC entry 4916 (class 2606 OID 25915)
-- Name: habitacion fk_h_idtipo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.habitacion
    ADD CONSTRAINT fk_h_idtipo FOREIGN KEY (id_tipo) REFERENCES public.tipo_habitacion(id_tipo);


--
-- TOC entry 4917 (class 2606 OID 25920)
-- Name: pago pago_id_reserva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago
    ADD CONSTRAINT pago_id_reserva_fkey FOREIGN KEY (id_reserva) REFERENCES public.reserva(id_reserva);


--
-- TOC entry 4918 (class 2606 OID 25925)
-- Name: reserva reserva_id_cliente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT reserva_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);


--
-- TOC entry 4919 (class 2606 OID 25930)
-- Name: reserva reserva_id_habitacion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT reserva_id_habitacion_fkey FOREIGN KEY (id_habitacion) REFERENCES public.habitacion(id_habitacion);


-- Completed on 2026-05-02 12:20:16

--
-- PostgreSQL database dump complete
--

\unrestrict 2kdbcJUMxhk9MQTLdPTgMYvBV3knVgWHPwrdCaOvXGz0DgQjLF3AzRP13jxcGj9

