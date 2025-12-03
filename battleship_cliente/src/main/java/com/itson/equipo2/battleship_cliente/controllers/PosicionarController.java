package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.exceptions.PosicionarNaveException;
import com.itson.equipo2.battleship_cliente.models.NaveModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.service.PosicionarNaveService;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 * Controlador para la fase de posicionamiento de naves del juego.
 * <p>
 * Gestiona las interacciones de la vista (solicitar posicionamiento) y delega
 * la lógica de negocio al Modelo y la comunicación al Servicio.
 * </p>
 */
public class PosicionarController {

    /**
     * Servicio responsable de la comunicación con el servidor.
     */
    private final PosicionarNaveService posicionarNaveService;

    private VistaController viewController;

    /**
     * Referencia al modelo de datos principal de la partida.
     */
    private final PartidaModel partidaModel;

    // --- CONSTRUCTOR ---
    /**
     * Inicializa el controlador con las dependencias necesarias.
     *
     * @param posicionarNaveService El servicio de comunicación para enviar la
     * flota al servidor.
     * @param partidaModel El modelo de datos de la partida.
     */
    public PosicionarController(PosicionarNaveService posicionarNaveService, PartidaModel partidaModel) {
        this.posicionarNaveService = posicionarNaveService;
        this.partidaModel = partidaModel;
    }

    // --- MÉTODOS PÚBLICOS ---
    /**
     * Intenta posicionar una nave en el tablero del jugador.
     * <p>
     * Delega la validación de límites y colisiones directamente al
     * {@code PartidaModel}.
     * </p>
     *
     * @param tipo El tipo de nave a posicionar.
     * @param fila La fila de inicio (coordenada Y).
     * @param col La columna de inicio (coordenada X).
     * @param esHorizontal {@code true} si la nave es horizontal, {@code false}
     * si es vertical.
     */
    public void intentarPosicionarNave(TipoNave tipo, int fila, int col, boolean esHorizontal) throws PosicionarNaveException {
        this.partidaModel.intentarPosicionarNavePropia(tipo, fila, col, esHorizontal);
    }

    /**
     * Confirma el posicionamiento de la flota del jugador y notifica al
     * servidor a través del servicio de posicionamiento.
     */
    public void confirmarPosicionamiento() {
        this.posicionarNaveService.confirmarPosicionamiento();

        if (viewController != null) {
            System.out.println("Cliente: Flota enviada. Esperando al rival...");
            viewController.cambiarPantalla("esperandoPosicionamiento");
        }
    }

    public NaveModel obtenerNaveEn(int fila, int columna) {
        // 1. Validar que tengamos un modelo y tablero cargado
        if (partidaModel == null || partidaModel.getTableroPropio() == null) {
            return null;
        }

        // (Opcional) Aquí podrías validar si el estado del juego permite editar
        // if (partidaModel.getEstado() != EstadoPartida.CONFIGURACION) return null;
        // 2. Pedir al modelo la nave (Delegación)
        return partidaModel.getTableroPropio().getNaveEnCasilla(fila, columna);
    }

    public void retirarNave(NaveModel nave) {
        if (partidaModel.getTableroPropio() != null) {
            partidaModel.getTableroPropio().eliminarNave(nave);
            // Notificar cambio para que la vista se repinte (Observer)
            partidaModel.notifyObservers(partidaModel);
        }
    }

    public void generarPosicionamientoAleatorio() {
        // 1. Limpiar tablero actual
        if (partidaModel.getTableroPropio() != null) {
            partidaModel.getTableroPropio().limpiarTablero();
        }

        java.util.Random random = new java.util.Random();

        // 2. Iterar por cada TIPO de nave
        for (TipoNave tipo : TipoNave.values()) {

            // --- CORRECCIÓN: Bucle interno según la cantidad de barcos de ese tipo ---
            int cantidad = tipo.getCantidadInicial();

            for (int i = 0; i < cantidad; i++) {
                boolean posicionado = false;
                int intentos = 0; // Seguridad para evitar bucles infinitos

                while (!posicionado && intentos < 100) {
                    int fila = random.nextInt(10);
                    int col = random.nextInt(10);
                    boolean horizontal = random.nextBoolean();

                    try {
                        // Intentamos posicionar
                        partidaModel.intentarPosicionarNavePropia(tipo, fila, col, horizontal);
                        posicionado = true; // ¡Éxito!
                    } catch (PosicionarNaveException e) {
                        // Si choca o se sale, intentamos de nuevo
                        intentos++;
                    }
                }

                if (!posicionado) {
                    System.err.println("No se pudo colocar una nave tipo " + tipo + " después de 100 intentos.");
                    // Opcional: Podrías reiniciar todo el proceso recursivamente si falla
                }
            }
        }

        // 3. Notificar cambios a la vista para que se pinten los barcos
        partidaModel.notifyObservers(partidaModel);
    }

    public void setViewController(VistaController viewController) {
        this.viewController = viewController;
    }

}
