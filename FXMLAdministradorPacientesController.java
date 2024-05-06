package controlnutricionaljfx.controlador;

import controlnutricionaljfx.modelo.dao.CatalogoDAO;
import controlnutricionaljfx.modelo.dao.PacienteDAO;
import controlnutricionaljfx.modelo.pojo.Estado;
import controlnutricionaljfx.modelo.pojo.Municipio;
import controlnutricionaljfx.modelo.pojo.Paciente;
import controlnutricionaljfx.utilidades.Constantes;
import controlnutricionaljfx.utilidades.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLAdministradorPacientesController implements Initializable {
    
    private int idNutriologo;
    private ObservableList<Paciente> pacientes;
    @FXML
    private TableView<Paciente> tvPacientes;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApPaterno;
    @FXML
    private TableColumn conApMaterno;
    @FXML
    private TableColumn colFechaNac;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colEstatura;
    @FXML
    private TableColumn colMunicipio;
    @FXML
    private ImageView imgRegresar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarValores(int idNutriologo){
        this.idNutriologo = idNutriologo;
        configurarTabla();
        cargarDatosPaciente();
    }

    public int getIdNutriologo() {
        return idNutriologo;
    }

    public void setIdNutriologo(int idNutriologo) {
        this.idNutriologo = idNutriologo;
    }
    
    private void configurarTabla(){
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        conApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory("fechaNacimiento"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colEstatura.setCellValueFactory(new PropertyValueFactory("estatura"));
        colMunicipio.setCellValueFactory(new PropertyValueFactory("municipio"));
        
    }
    
    private void cargarDatosPaciente(){
        pacientes = FXCollections.observableArrayList();
        HashMap<String,Object> respuesta = PacienteDAO.obtenerPacientesNutriologo(idNutriologo);
        boolean isError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if(!isError){
            ArrayList<Paciente> pacientesBD = (ArrayList<Paciente>) respuesta.get("pacientes");
            pacientes.addAll(pacientesBD);
            tvPacientes.setItems(pacientes);
        }else{
            Utils.mostrarAlertaSimple("Error", "" + respuesta.get(Constantes.KEY_MENSAJE), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioPaciente(null);
        
    }

    @FXML
    private void btnClicEditar(ActionEvent event) {
        Paciente pacienteSeleccionado = tvPacientes.getSelectionModel().getSelectedItem();
        if(pacienteSeleccionado != null){
            irFormularioPaciente(pacienteSeleccionado);
        }else{
            Utils.mostrarAlertaSimple("Seleccciona un paciente", 
                    "Para actualizar la informacion de un paciente primero debes "
                            + "seleccionarlo en la tabla", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) {
        
    }
    
    private void irFormularioPaciente(Paciente pacienteEdicion){
        try {
            Stage escenario = new Stage();
            FXMLLoader loader = Utils.obtenerLoader("vista/FXMLFormularioPaciente.fxml");
            Parent root = loader.load();
            FXMLFormularioPacienteController controlador = loader.getController();
            controlador.inicializarValores(pacienteEdicion);
            Scene escena = new Scene(root);
            escenario.setScene(escena);
            escenario.setTitle("Pacientes");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void imgClicRgresar(MouseEvent event) {
        Stage escenarioAdminPacientes = (Stage) imgRegresar.getScene().getWindow();
        escenarioAdminPacientes.close();
    }
}