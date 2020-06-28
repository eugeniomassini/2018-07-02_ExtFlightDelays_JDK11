package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare ai branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField distanzaMinima;

    @FXML
    private Button btnAnalizza;

    @FXML
    private ComboBox<Airport> cmbBoxAeroportoPartenza;

    @FXML
    private Button btnAeroportiConnessi;

    @FXML
    private TextField numeroVoliTxtInput;

    @FXML
    private Button btnCercaItinerario;

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	
    	String minimaS = distanzaMinima.getText();
    	
    	Double minima=0.0; 
    	try {
			minima = Double.parseDouble(minimaS);
		} catch (NumberFormatException e) {
			txtResult.appendText("Errore inserisci un numero");
			e.printStackTrace();
			return;
		}
    	
    	model.creaGrafo(minima);
    	
    	cmbBoxAeroportoPartenza.getItems().addAll(model.getAeroporti());
    	
    	
    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {

    	Airport partenza = cmbBoxAeroportoPartenza.getValue();
    	
    	if(partenza==null) {
    		txtResult.appendText("Errore");
    		return;
    	}
    	
    	for(Vicino v: model.getVicini(partenza)) {
    		txtResult.appendText(v+"\n");
    	}
    	
    	
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	String migliaMassimeS = numeroVoliTxtInput.getText();
    	
    	Double migliaMassime = 0.0;
    	
    	try {
			migliaMassime = Double.parseDouble(migliaMassimeS);
		} catch (NumberFormatException e) {
			txtResult.appendText("Errore inserisci un numero");
			e.printStackTrace();
			return;
		}
    	
    	Airport partenza = cmbBoxAeroportoPartenza.getValue();
    	
    	if(partenza==null) {
    		txtResult.appendText("Errore, seleziona un aeroporto di partenza");
    		return;
    	}
    	
    	model.cerca(partenza, migliaMassime);
    	
    	for(Airport a: model.getBest()) {
    		txtResult.appendText(a+"\n");
    	}

    	txtResult.appendText("Miglia usate "+model.getMigliaUsate());

    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
