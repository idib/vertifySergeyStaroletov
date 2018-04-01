package com;

import com.BTree.BTree;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.util.ArrayList;

/**
 * \brief Класс GUI
 * <p>
 * Класс создающий графический интерфес
 */
public class Main extends Application {
	private TextField TextKey;
	private TextField TextT;
	private TextField TextMin;
	private TextField TextMax;
	private Group TreePanel;
	private VBox StagePanel;
	private Scene scene;
	private BTree T;
	private double SY = 30;
	private double SX = 60;
	private double distX = 15;
	private double distY = 60;
	private int numsT = 2;

	/**
	 * Точка входа
	 * Запуск инициализация графического интерфейса
	 */
	public static void main(String[] args) {
		launch(args);
	}   // запуск


	/**
	 * Обновление графического представления данных
	 */
	private void refreshTree() {        // обновление дерева
		T.refreshXY();
		TreePanel.getChildren().clear();
		ArrayList<Shape> r = T.getShapeTree();
		TreePanel.getChildren().addAll(r);
	}

	/**
	 * Обработчик событий.
	 * Создание нового дерева
	 */
	private void TNew() {           //инициализация нового дерева
		System.out.println("New Tree");
		T = new BTree(Integer.parseInt(TextT.getText()));
		T.setSX(SX);
		T.setSY(SY);
		T.setDistX(distX);
		T.setDistY(distY);
		refreshTree();
	}

	/**
	 * Обработчик событий.
	 * Добавление новго элемента в дерево.
	 */
	private void InsertElem() {     //вставка элемента в дерево
		if (TextKey.getText().length() == 0)
			return;
		int curInt = Integer.parseInt(TextKey.getText());
		if (!T.containsKey(curInt)) {
			System.out.println("+ " + TextKey.getText());
			T.put(curInt, curInt);
			refreshTree();
			TextKey.setText("");
		}
	}

	/**
	 * Обработчик событий.
	 * Удаление элемента из дерева.
	 */
	private void DelElem() {        //удаление элемента в дереве
		if (TextKey.getText().length() == 0)
			return;
		int curInt = Integer.parseInt(TextKey.getText());
		if (T.containsKey(curInt)) {
			System.out.println("- " + TextKey.getText());
			T.del(curInt);
			refreshTree();
			TextKey.setText("");
		}
	}


	/**
	 * Обработчик событий.
	 * Удаление элементов попавших в определнный диапозон.
	 */
	private void DelOnSelect() {     //удаление элемента из определенного диапозона в дереве
		if (TextMax.getText().length() == 0 && TextMin.getText().length() == 0)
			return;
		int curmin = Integer.parseInt(TextMin.getText());
		int curmax = Integer.parseInt(TextMax.getText());
		Integer cur = (Integer) T.maxOnSelect(curmin, curmax);
		if (cur != null) {
			System.out.println("- " + cur.toString());
			T.del(cur);
			refreshTree();
			TextKey.setText("");
		}

	}

	/**
	 *  Инициализация графического интерфейса
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {        //Инициализация формы
		StagePanel = new VBox();
		HBox menu = new HBox();

		TreePanel = new Group();
		TreePanel.setLayoutX(0);
		TreePanel.setLayoutY(60);

		TextKey = new TextField();
		TextKey.setMaxWidth(150);
		TextKey.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				InsertElem();
			} else if (event.getCode().equals(KeyCode.DELETE)) {
				DelElem();
			}
		});

		TextT = new TextField();
		TextT.setText(Integer.toString(numsT));
		TextT.setMaxWidth(40);

		Text TMi = new Text("min:");

		TextMin = new TextField();
		TextMin.setMaxWidth(40);
		TextMin.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.DELETE))
				DelOnSelect();
		});

		Text TMa = new Text("max:");

		TextMax = new TextField();
		TextMax.setMaxWidth(40);
		TextMax.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.DELETE) || event.getCode().equals(KeyCode.ENTER))
				DelOnSelect();
		});

//        Button btnInsert = new Button();
//        //btnInsert.setLayoutX(150);
//        btnInsert.setText("Добавить");
//        btnInsert.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                InsertElem();
//            }
//        });
//        Button btnDelete = new Button();
//        //btnDelete.setLayoutX(300);
//        btnDelete.setText("Удалить");
//        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                DelElem();
//            }
//        });

		Button btnNew = new Button();
		//btnNew.setLayoutX(450);
		btnNew.setText("Новое дерево");
		btnNew.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TNew();
			}
		});


		TNew();


		menu.getChildren().add(TextKey);
//        menu.getChildren().add(btnInsert);
//        menu.getChildren().add(btnDelete);
		menu.getChildren().add(btnNew);
		menu.getChildren().add(TextT);
		menu.getChildren().add(TMi);
		menu.getChildren().add(TextMin);
		menu.getChildren().add(TMa);
		menu.getChildren().add(TextMax);
		StagePanel.getChildren().add(menu);
		StagePanel.getChildren().add(TreePanel);
		scene = new Scene(StagePanel, 600, 300);
		primaryStage.setTitle("main/com.BTree.BTree");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
