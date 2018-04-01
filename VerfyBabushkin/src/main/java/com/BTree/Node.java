package com.BTree;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by idib on 16.11.16.
 */
final class Node<K extends Comparable<K>, V> extends AbstractChildren {
    int Count;
    Entry<K, V>[] entry;
    Node<K, V>[] children;
    boolean leaf;

	/**
	 * Конструктор узла дерева с максимальным количеством элементов 2*t - 1
	 * @param t 2*t-1 максимальное количеством элементов узла
	 */
	Node(int t) {
        Count = 0;
        leaf = false;
        children = new Node[2 * t];
        entry = new Entry[2 * t - 1];
    }

    @Override
    public double getWidth() {
        double sum = 0;
        for (int i = 0; i < Count; i++) {
            sum += entry[i].getWidth();
        }
        width = sum;
        return sum;
    }

    @Override
    public double getHeight() {
        for (int i = 0; i < Count; i++) {
            return entry[i].getHeight();
        }
        return 0;
    }

    @Override
    void setX(double x) {
        super.setX(x);
        for (int i = 0; i < Count; i++) {
            entry[i].setX(x);
            x += entry[i].getWidth();
        }
    }

    @Override
    void setY(double y) {
        super.setY(y);
        for (int i = 0; i < Count; i++) {
            entry[i].setY(y);
        }
    }

	/**
	 * Плолучение графического представления узла
	 *
	 * @return Массив фигур
	 */
    ArrayList<Shape> getShape() {
        ArrayList<Shape> res = new ArrayList<>();
        res.addAll(getLine());
        for (int i = 0; i < Count; i++) {   // add Names entry
            res.addAll(entry[i].getShape());
        }
        return res;
    }

	/**
	 * Плолучение графического стрелкок от текущего узла до дочерних
	 *
	 * @return Массив фигур
	 */
    private ArrayList<Shape> getLine() {
        ArrayList<Shape> res = new ArrayList<>();
        double sx = x;
        double sy = y + getHeight();
        double ex, ey;
        for (int i = 0; i <= Count; i++) {
            if (children[i] != null) {
                ex = children[i].x + children[i].width / 2;
                ey = children[i].y;
                res.addAll(getLP(sx, sy, ex, ey));
            }
            if (i != Count)
                sx += entry[i].getWidth();
        }
        return res;
    }

	/**
	 * Плолучение графического стрелки по координатам
	 * @param sX X начала стелки
	 * @param sY Y начала стелки
	 * @param eX X конца стелки
	 * @param eY Y конца стелки
	 * @return Массив фигур
	 */
    private ArrayList<Line> getLP(double sX, double sY, double eX, double eY) {
        ArrayList<Line> res = new ArrayList<>();
        double deg = Math.atan2(eY - sY, eX - sX);
        res.add(new Line(eX, eY, sX, sY));

        deg -= Math.PI / 6;
        double nX = lengPointLint * Math.cos(deg);
        double nY = lengPointLint * Math.sin(deg);
        res.add(new Line(eX - nX, eY - nY, eX, eY));

        deg += Math.PI / 3;
        nX = lengPointLint * Math.cos(deg);
        nY = lengPointLint * Math.sin(deg);
        res.add(new Line(eX - nX, eY - nY, eX, eY));
        return res;
    }
}
