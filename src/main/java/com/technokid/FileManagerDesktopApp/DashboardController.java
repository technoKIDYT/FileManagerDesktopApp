package com.technokid.FileManagerDesktopApp;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.technokid.FileManagerDesktopApp.Utility.Utility;
import com.technokid.FileManagerDesktopApp.model.Drive;
import com.technokid.FileManagerDesktopApp.model.FileEnhanced;
import com.technokid.FileManagerDesktopApp.model.FileType;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

	@FXML
	private Button btnFiles;

	@FXML
	private Button btnStorage;

	@FXML
	private ComboBox<Drive> drpDrives;

	@FXML
	private TabPane mainTabPane;

	@FXML
	private PieChart spaceChart;

	@FXML
	private Label lblUsed, lblFree;

	@FXML
	private HBox hboxStatus;

	@FXML
	private ImageView lnkApps;

	@FXML
	private ImageView lnkDocs;

	@FXML
	private ImageView lnkImages;

	@FXML
	private ImageView lnkMusic;

	@FXML
	private ImageView lnkVideos;

	@FXML
	private ImageView lnkZip;

	@FXML
	private HBox hboxLoad;

	@FXML
	private TextField inpSearch;

	@FXML
	private TableView<FileEnhanced> listDataTableView;

	@FXML
	private Label lblFileType;

	@FXML
	private TextField filterField;

	@FXML
	private Label lblLoad;

	@FXML
	private ImageView btnMax;

	@FXML
	private ImageView btnMin;

	@FXML
	private ImageView btnClose;

	private final TableColumn<FileEnhanced, File> fileColumn = new TableColumn<FileEnhanced, File>("File");

	private Utility utility;
	private ObservableList<PieChart.Data> pieChartData;
	private ArrayList<Drive> drives;
	private ObservableList<FileEnhanced> origFileEnhanceds;
	private String fileType;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		utility = new Utility();
		loadAppData();
	}

	public void loadAppData() {
		drives = utility.getAllDrives();
		drpDrives.getItems().addAll(drives);
		hboxStatus.setVisible(false);
		hboxLoad.setVisible(false);
		origFileEnhanceds = FXCollections.observableArrayList();
		fileColumn.setCellValueFactory(new PropertyValueFactory<>("file"));
		listDataTableView.getColumns().add(fileColumn);
		listDataTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		mainTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
				System.out.println("Tab Selection changed " + t1.getText());
				if (t1.getText().equalsIgnoreCase("Files")) {
					btnStorage.setStyle("-fx-background-color:#F1F1F3;-fx-background-radius:10;-fx-text-fill:#8a8686;");
					btnFiles.setStyle("-fx-background-color:#FC7955;-fx-background-radius:10;-fx-text-fill:#FFFFFF;");
				} else if (t1.getText().equalsIgnoreCase("Storage")) {
					btnFiles.setStyle("-fx-background-color:#F1F1F3;-fx-background-radius:10;-fx-text-fill:#8a8686;");
					btnStorage.setStyle("-fx-background-color:#FC7955;-fx-background-radius:10;-fx-text-fill:#FFFFFF;");
				}
			}
		});

		final ContextMenu randomListContextMenu = new ContextMenu();
		MenuItem replaceCardMenuItem = new MenuItem("Show in explorer");
		replaceCardMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showInExplorer(listDataTableView.selectionModelProperty().get().getSelectedItem().getFile());
			}
		});
		randomListContextMenu.getItems().add(replaceCardMenuItem);

		listDataTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					randomListContextMenu.show(listDataTableView, event.getScreenX(), event.getScreenY());
				}
			}
		});

	}

	public void showInExplorer(File f) {
		try {
			Runtime.getRuntime().exec("explorer.exe  /select," + f.getAbsolutePath());
		} catch (Exception ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText(ex.getMessage());
			alert.show();
		}
	}

	public void loadChartData() {
		pieChartData = FXCollections.observableArrayList();
		Drive drive = drpDrives.getSelectionModel().getSelectedItem();
		PieChart.Data usedData = new PieChart.Data("Used Space " + drive.getUsedPer() + " %", drive.getDblUsedSpace());
		PieChart.Data remData = new PieChart.Data("Free Space " + drive.getRemPer() + " %", drive.getDblFreeSpace());
		pieChartData.add(usedData);
		pieChartData.add(remData);
		spaceChart.setData(pieChartData);
		hboxStatus.setVisible(true);
		lblUsed.setText(drive.getUsedSpace());
		lblFree.setText(drive.getFreeSpace());
	}

	@SuppressWarnings("exports")
	public void changeTabs(ActionEvent actionEvent) {
		Button button = (Button) actionEvent.getSource();
		if (button.equals(btnStorage)) {
			btnFiles.setStyle("-fx-background-color:#F1F1F3;-fx-background-radius:10;-fx-text-fill:#8a8686;");
			btnStorage.setStyle("-fx-background-color:#FC7955;-fx-background-radius:10;-fx-text-fill:#FFFFFF;");
			System.out.println("Storage");
			mainTabPane.getSelectionModel().select(0);
		}
		if (button.equals(btnFiles)) {
			btnStorage.setStyle("-fx-background-color:#F1F1F3;-fx-background-radius:10;-fx-text-fill:#8a8686;");
			btnFiles.setStyle("-fx-background-color:#FC7955;-fx-background-radius:10;-fx-text-fill:#FFFFFF;");
			System.out.println("Files");
			mainTabPane.getSelectionModel().select(1);
		}
	}

	public void goToFile() {
		btnStorage.setStyle("-fx-background-color:#F1F1F3;-fx-background-radius:10;-fx-text-fill:#8a8686;");
		btnFiles.setStyle("-fx-background-color:#FC7955;-fx-background-radius:10;-fx-text-fill:#FFFFFF;");
		System.out.println("Files");
		mainTabPane.getSelectionModel().select(1);
	}

	public void goToStorage() {
		btnFiles.setStyle("-fx-background-color:#F1F1F3;-fx-background-radius:10;-fx-text-fill:#8a8686;");
		btnStorage.setStyle("-fx-background-color:#FC7955;-fx-background-radius:10;-fx-text-fill:#FFFFFF;");
		System.out.println("Storage");
		mainTabPane.getSelectionModel().select(0);
	}

	@SuppressWarnings("exports")
	public void loadData(MouseEvent event) {
		origFileEnhanceds.clear();
		if (drpDrives.getSelectionModel().getSelectedItem() != null) {
			ImageView imageView = (ImageView) event.getSource();
			if (imageView.equals(lnkDocs)) {
				fileType = FileType.Documents.name();
				utility.reset();
				listDataTableView.getItems().clear();
				mainTabPane.setDisable(true);
				hboxLoad.setVisible(true);
				lblLoad.setText("Wait while fetching " + fileType + " files...");
				Thread thread = new Thread(() -> {
					ObservableList<FileEnhanced> fileEnhanceds = utility.recursiveGetAllDocumentFiles(
							drpDrives.getSelectionModel().getSelectedItem().getFile().listFiles(), 0);
					Platform.runLater(() -> {
						lblFileType.setText(FileType.Documents.name() + " - " + fileEnhanceds.size());
						System.out.println("Total Documents :-" + fileEnhanceds.size());
						listDataTableView.setItems(fileEnhanceds);
						origFileEnhanceds.addAll(fileEnhanceds);
						goToFile();
						mainTabPane.setDisable(false);
						hboxLoad.setVisible(false);
					});
				});
				thread.start();

			} else if (imageView.equals(lnkImages)) {
				fileType = FileType.Images.name();
				utility.reset();
				listDataTableView.getItems().clear();
				mainTabPane.setDisable(true);
				hboxLoad.setVisible(true);
				lblLoad.setText("Wait while fetching " + fileType + " files...");
				Thread thread = new Thread(() -> {
					ObservableList<FileEnhanced> fileEnhanceds = utility.recursiveGetAllImageFiles(
							drpDrives.getSelectionModel().getSelectedItem().getFile().listFiles(), 0);
					Platform.runLater(() -> {
						lblFileType.setText(FileType.Images.name() + " - " + fileEnhanceds.size());
						System.out.println("Total Images :-" + fileEnhanceds.size());
						listDataTableView.setItems(fileEnhanceds);
						origFileEnhanceds.addAll(fileEnhanceds);
						goToFile();
						mainTabPane.setDisable(false);
						hboxLoad.setVisible(false);
					});
				});
				thread.start();
			} else if (imageView.equals(lnkVideos)) {
				fileType = FileType.Videos.name();
				utility.reset();
				listDataTableView.getItems().clear();
				mainTabPane.setDisable(true);
				hboxLoad.setVisible(true);
				lblLoad.setText("Wait while fetching " + fileType + " files...");
				Thread thread = new Thread(() -> {
					ObservableList<FileEnhanced> fileEnhanceds = utility.recursiveGetAllVideoFiles(
							drpDrives.getSelectionModel().getSelectedItem().getFile().listFiles(), 0);
					Platform.runLater(() -> {
						lblFileType.setText(FileType.Videos.name() + " - " + fileEnhanceds.size());
						System.out.println("Total Videos :-" + fileEnhanceds.size());
						listDataTableView.setItems(fileEnhanceds);
						origFileEnhanceds.addAll(fileEnhanceds);
						goToFile();
						mainTabPane.setDisable(false);
						hboxLoad.setVisible(false);
					});
				});
				thread.start();
			} else if (imageView.equals(lnkZip)) {
				fileType = FileType.Archives.name();
				utility.reset();
				listDataTableView.getItems().clear();
				mainTabPane.setDisable(true);
				hboxLoad.setVisible(true);
				lblLoad.setText("Wait while fetching " + fileType + " files...");
				Thread thread = new Thread(() -> {
					ObservableList<FileEnhanced> fileEnhanceds = utility.recursiveGetAllArchFiles(
							drpDrives.getSelectionModel().getSelectedItem().getFile().listFiles(), 0);
					Platform.runLater(() -> {
						lblFileType.setText(FileType.Archives.name() + " - " + fileEnhanceds.size());
						System.out.println("Total Arch :-" + fileEnhanceds.size());
						listDataTableView.setItems(fileEnhanceds);
						origFileEnhanceds.addAll(fileEnhanceds);
						goToFile();
						mainTabPane.setDisable(false);
						hboxLoad.setVisible(false);
					});
				});
				thread.start();
			} else if (imageView.equals(lnkMusic)) {
				fileType = FileType.Music.name();
				utility.reset();
				listDataTableView.getItems().clear();
				mainTabPane.setDisable(true);
				hboxLoad.setVisible(true);
				lblLoad.setText("Wait while fetching " + fileType + " files...");
				Thread thread = new Thread(() -> {
					ObservableList<FileEnhanced> fileEnhanceds = utility.recursiveGetAllMusicFiles(
							drpDrives.getSelectionModel().getSelectedItem().getFile().listFiles(), 0);
					Platform.runLater(() -> {
						lblFileType.setText(FileType.Music.name() + " - " + fileEnhanceds.size());
						System.out.println("Total Music :-" + fileEnhanceds.size());
						listDataTableView.setItems(fileEnhanceds);
						origFileEnhanceds.addAll(fileEnhanceds);
						goToFile();
						mainTabPane.setDisable(false);
						hboxLoad.setVisible(false);
					});
				});
				thread.start();
			} else if (imageView.equals(lnkApps)) {
				fileType = FileType.Apps.name();
				utility.reset();
				listDataTableView.getItems().clear();
				mainTabPane.setDisable(true);
				hboxLoad.setVisible(true);
				lblLoad.setText("Wait while fetching " + fileType + " files...");
				Thread thread = new Thread(() -> {
					ObservableList<FileEnhanced> fileEnhanceds = utility.recursiveGetAllAppFiles(
							drpDrives.getSelectionModel().getSelectedItem().getFile().listFiles(), 0);
					Platform.runLater(() -> {
						lblFileType.setText(FileType.Apps.name() + " - " + fileEnhanceds.size());
						System.out.println("Total Apps/Setup :-" + fileEnhanceds.size());
						listDataTableView.setItems(fileEnhanceds);
						origFileEnhanceds.addAll(fileEnhanceds);
						goToFile();
						mainTabPane.setDisable(false);
						hboxLoad.setVisible(false);
					});
				});
				thread.start();
			}
			FilteredList<FileEnhanced> filteredData = new FilteredList<FileEnhanced>(origFileEnhanceds, s -> true);
			inpSearch.textProperty().addListener((obs, oldVal, newVal) -> {
				filteredData.setPredicate(new Predicate<FileEnhanced>() {
					@Override
					public boolean test(FileEnhanced t) {
						if (newVal == null || newVal.isEmpty()) {
							return true;
						}
						String newFilterVal = newVal.toLowerCase();
						if (t.getFile().getAbsolutePath().toLowerCase().contains(newFilterVal)) {
							return true;
						}
						return false;
					}
				});
				lblFileType.setText(fileType + " - " + filteredData.size());
				SortedList<FileEnhanced> sortedList = new SortedList<FileEnhanced>(filteredData);
				sortedList.comparatorProperty().bind(listDataTableView.comparatorProperty());
				listDataTableView.setItems(sortedList);
			});
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Mandatory");
			alert.setContentText("Please select drive");
			alert.show();
			drpDrives.requestFocus();
		}
	}

	@SuppressWarnings("exports")
	public void minMaxClose(MouseEvent mouseEvent) {
		if (mouseEvent.getSource() instanceof ImageView) {
			ImageView btnSrc = (ImageView) mouseEvent.getSource();
			if (btnSrc.equals(btnMin)) {
				Stage stage = (Stage) btnSrc.getScene().getWindow();
				stage.setIconified(true);
			} else if (btnSrc.equals(btnMax)) {
				Stage stage = (Stage) btnSrc.getScene().getWindow();
				if (!stage.isMaximized()) {
					stage.setMaximized(true);
				} else {
					stage.setMaximized(false);
				}
			} else if (btnSrc.equals(btnClose)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation ?");
				alert.setContentText("Do you really want to exit ?");
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get() == ButtonType.OK) {
					System.exit(0);
				}
			}
		}
	}
}
