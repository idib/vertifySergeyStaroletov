package com.BTree;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Значение типа V хранимое в дереве под ключем типа K
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
class Entry<K extends Comparable<K>, V> extends AbstractChildren {
	private K key;
	private V value;
	private Text text;
	private Rectangle bounds;

	/**
	 * Конструктор класса Entry
	 *
	 * @param key   ключ
	 * @param value значение
	 */
	Entry(K key, V value) {
		this.key = key;
		text = new Text();
		bounds = new Rectangle();
		setValue(value);
		bounds.setFill(Color.TRANSPARENT);
		bounds.setStroke(Color.BLACK);
	}

	/**
	 * Ключ
	 *
	 * @return ключ
	 */
	K getKey() {
		return key;
	}

	/**
	 * Значение хранимое под данным ключом
	 *
	 * @return занчение
	 */
	V getValue() {
		return value;
	}

	/**
	 * установить новое значение
	 *
	 * @param value значение
	 */
	void setValue(V value) {
		this.value = value;
		text.setText(value.toString());
		bounds.setWidth(text.getLayoutBounds().getWidth() + 2 * padding);
		bounds.setHeight(text.getLayoutBounds().getHeight() + 2 * padding);
		width = bounds.getWidth();
		height = bounds.getHeight();
	}

	@Override
	void setX(double x) {
		super.setX(x);
		text.setX(x + padding);
		bounds.setX(x);
	}

	@Override
	void setY(double y) {
		super.setY(y);
		text.setY(y + padding + text.getLayoutBounds().getHeight());
		bounds.setY(y);
	}

	/**
	 * Проверка на меньши ли данный ключ чем передаваемый
	 *
	 * @param k1 ключ
	 * @return k1 < Entry.key
	 */
	boolean less(K k1) {
		return k1.compareTo(key) > 0;
	}

	/**
	 * Проверка на равенство ключей
	 *
	 * @param k1 ключ
	 * @return k1 == Entry.key
	 */
	boolean eq(K k1) {
		return k1.compareTo(key) == 0;
	}

	/**
	 * Проверка на больше ли данный ключ чем передаваемый
	 *
	 * @param k1 ключ
	 * @return k1 > Entry.key
	 */
	boolean larger(K k1) {
		return k1.compareTo(key) < 0;
	}

	/**
	 * Плолучение графического представления елемента узла
	 *
	 * @return Массив фигур
	 */
	ArrayList<Shape> getShape() {
		ArrayList<Shape> res = new ArrayList<>();
		res.add(text);
		res.add(bounds);
		return res;
	}
}
