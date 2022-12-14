package application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		BorderPane borderPane = new BorderPane();
		TabPane tb = new TabPane();
		tb.setTabMaxWidth(100);
		tb.setPrefSize(1200, 630);
//		tb.setSide(Side.TOP);
		Createfirsttab(tb);
		final Tab newtab = new Tab();
		newtab.setText(" + ");
		newtab.setClosable(false);
		tb.getTabs().addAll(newtab);

		MenuBar menubar = new MenuBar();
		Menu menu = new Menu("Menu");
		MenuItem menuItemHome = new MenuItem("Home");
		MenuItem menuItemHistory = new MenuItem("History");
		MenuItem menuItemDownload = new MenuItem("Download");
		MenuItem menuItemAbout = new MenuItem("About");

		Button backButton = new Button("<");
		Button forwardButton = new Button(">");
		Button reloadButton = new Button("Reload");
		Button goButton = new Button("Go");

		menu.getItems().add(menuItemHome);
		menu.getItems().add(menuItemHistory);
		menu.getItems().add(menuItemDownload);
		menu.getItems().add(menuItemAbout);
		menubar.getMenus().add(menu);

		ScrollPane sp = new ScrollPane();
		final TextField urlField = new TextField("http://");
		// WebView - to display, browse web pages.
		WebView webView = new WebView();
		webView.setPrefHeight(630);
		webView.setPrefWidth(1180);
		final WebEngine webEngine = webView.getEngine();
		WebHistory history = webEngine.getHistory();

		HBox hBox = new HBox(2);
		hBox.getChildren().setAll(backButton, forwardButton, reloadButton, urlField, goButton, menubar);
		HBox.setHgrow(urlField, Priority.ALWAYS);

		final VBox vBox = new VBox(1);
		sp.setContent(webView);
		vBox.getChildren().setAll(hBox, sp);
		VBox.setVgrow(webView, Priority.ALWAYS);

		backButton.setOnAction(e -> {
			ObservableList<WebHistory.Entry> entries = history.getEntries();
			history.go(-1);
			urlField.setText(entries.get(history.getCurrentIndex()).getUrl());
		});

		forwardButton.setOnAction(e -> {
			ObservableList<WebHistory.Entry> entries = history.getEntries();
			history.go(+1);
			urlField.setText(entries.get(history.getCurrentIndex()).getUrl());
		});

		urlField.setOnAction(e -> {
			webEngine.load(
					urlField.getText().startsWith("http://") ? urlField.getText() : "http://" + urlField.getText());
		});

		goButton.setOnAction(e -> {
			webEngine.load(
					urlField.getText().startsWith("http://") ? urlField.getText() : "http://" + urlField.getText());
		});

		reloadButton.setOnAction(e -> {
			webEngine.reload();
		});

		menuItemHome.setOnAction(e -> {
			webEngine.load("https://www.google.com");
		});

		menuItemDownload.setOnAction(e -> {
			showDownloadPage(primaryStage);
		});

		menuItemHistory.setOnAction(e -> {
			showHistoryPage(primaryStage, history, webEngine);
		});

		menuItemAbout.setOnAction(e -> {
			showAboutPage(primaryStage);
		});

		tb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldSelectedTab, Tab newSelectedTab) {
				if (newSelectedTab == newtab) {
					Tab tab = new Tab();
					tab.setText("New tab");
					// webEngine.load(DEFAULT_URL);

					webEngine.locationProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
							urlField.setText(newValue);
							tab.setText(urlField.getText());
						}
					});

					tab.setContent(vBox);
					final ObservableList<Tab> tabs = tb.getTabs();
					tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
					tabs.add(tabs.size() - 1, tab);
					tb.getSelectionModel().select(tab);
				}
			}
		});

		borderPane.setCenter(tb);
		root.getChildren().add(borderPane);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/web_search_engine.png")));
		primaryStage.setTitle("My browser");
		primaryStage.show();
	}

	private Tab Createfirsttab(TabPane tb) {
		Tab stab = new Tab(" Wellcome ");
		Label label = new Label();
		label.setText("\n\t\t\t To start browsing, click on New Tab.");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		stab.setContent(label);
		tb.getTabs().add(stab);
		tb.getSelectionModel().select(stab);
		return stab;
	}

	private void showDownloadPage(Stage stage) {
		Label labelUrl = new Label("enter the url to download");
		TextField url = new TextField();
		Label labelFileName = new Label("name");
		TextField fileName = new TextField();
		Button btn_download = new Button("Download");
		Button btn_exit = new Button("Exit");
		Label labelToast = new Label();

		VBox vBox = new VBox(10);
		vBox.getChildren().setAll(labelUrl, url, labelFileName, fileName, btn_download, btn_exit, labelToast);
		Scene secondScene = new Scene(vBox, 430, 230);
		// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.getIcons().add(new Image(Main.class.getResourceAsStream("/file.png")));
		newWindow.setScene(secondScene);

		// Set position of second window, related to primary window.
		newWindow.setX(stage.getX() + 200);
		newWindow.setY(stage.getY() + 100);

		newWindow.show();

		btn_download.setOnAction(e -> {
			String link = url.getText().toString().trim();
			String name = fileName.getText().toString().trim();
			if (link == "" && name == "") {
				labelToast.setText("please enter url and namefile!");
			} else {
				downloadFile(link, name, labelToast);
				System.out.println("Downloading");
			}
		});

		btn_exit.setOnAction(e -> {
			newWindow.close();
		});
	}

	private void showAboutPage(Stage stage) {
		Label label = new Label("-Version 1.0" + "\n" + "?? 2022 Akiroku Corporation.All rights reserved.");
		VBox vBox = new VBox(10);
		vBox.getChildren().setAll(label);
		Scene newScene = new Scene(vBox, 430, 100);

		Stage newStage = new Stage();
		newStage.getIcons().add(new Image(Main.class.getResourceAsStream("/information.png")));
		newStage.setTitle("About");
		newStage.setScene(newScene);

		newStage.setX(stage.getX() + 200);
		newStage.setY(stage.getY() + 100);

		newStage.show();
	}

	private void showHistoryPage(Stage stage, WebHistory history, WebEngine webEngine) {
		ObservableList<WebHistory.Entry> entries = history.getEntries();
		ListView<String> listUrl = new ListView<>();
		String url;
		for (WebHistory.Entry entry : entries) {
			url = (entry.getUrl() + " " + entry.getLastVisitedDate() + "\n");
			listUrl.getItems().addAll(url);
		}

		VBox vBox = new VBox(10);
		vBox.getChildren().setAll(listUrl);
		Scene newScene = new Scene(vBox, 730, 400);

		Stage newStage = new Stage();
		newStage.getIcons().add(new Image(Main.class.getResourceAsStream("/history.png")));
		newStage.setTitle("History");
		newStage.setScene(newScene);

		newStage.setX(stage.getX() + 200);
		newStage.setY(stage.getY() + 100);

		newStage.show();
	}

	private void downloadFile(String link, String namefile, Label toast) {
		try {
			URL url = new URL(link);
			InputStream inputStream = url.openStream();
			FileOutputStream fileOutputStream = new FileOutputStream("D:\\Download\\" + namefile);
			byte[] bytes = new byte[1024];
			int len;
			while ((len = inputStream.read(bytes)) != -1) {
				fileOutputStream.write(bytes, 0, len);
			}
			fileOutputStream.close();
			inputStream.close();
			toast.setText("download complete.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
