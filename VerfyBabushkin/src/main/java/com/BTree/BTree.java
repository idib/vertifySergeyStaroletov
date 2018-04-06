package com.BTree;

import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * B дерево с ключем типа K и значением типа V
 * @param <K> тип ключа, сравнимый
 * @param <V> значение
 */
public class BTree<K extends Comparable<K>, V> {
	private Node<K, V> Root;
	private int T = 4;
	private double sX;
	private double sY;
	private double distX = 45;
	private double distY = 45;
	private int size = 0;

	public int getSize() {
		return size;
	}

	/**
	 * Конструктор дерева.
	 * Значение 2*t-1 будет максимальним для количеством элементов в одном узле.
	 *
	 * @param t 2*t-1 максимальним для количеством элементов в одном узле.
	 */
	public BTree(int t) {
		T = t;
		Root = new Node(t);
		Root.leaf = true;
	}

	/**
	 * Получение стартовой координаты X
	 *
	 * @return стартовая координата X
	 */
	public double getSX() {
		return sX;
	}

	/**
	 * Установка стартовой координаты Y для рассчета позиции дерева
	 *
	 * @param sx
	 */
	public void setSX(double sx) {
		this.sX = sx;
	}

	/**
	 * Получение стартовой координаты Y
	 *
	 * @return стартовая координата Y
	 */
	public double getSY() {
		return sY;
	}

	/**
	 * Установка стартовой координаты Y для рассчета позиции дерева
	 *
	 * @param sy стартовая координата Y
	 */
	public void setSY(double sy) {
		this.sY = sy;
	}

	/**
	 * Установка дистанции по X между узлами.
	 *
	 * @param distX
	 */
	public void setDistX(double distX) {
		this.distX = distX;
	}

	/**
	 * Установка дистанции по Y между узлами.
	 *
	 * @param distY
	 */
	public void setDistY(double distY) {
		this.distY = distY;
	}

	/**
	 * Получение значения по ключу
	 *
	 * @param k ключ
	 * @return значние
	 */
	public V get(K k) {
		if (k == null) throw new IllegalArgumentException("argument to get() is null");
		return search(Root, k).getValue();
	}

	/**
	 * Вставка пары ключ-значение в дерево
	 *
	 * @param k   Ключ
	 * @param val Значение
	 */
	public void put(K k, V val) {
		Entry<K, V> cur = search(Root, k);
		if (cur != null) {
			cur.setValue(val);
		} else {
			size++;
			Node<K, V> r = Root;
			if (r.Count == 2 * T - 1) {
				Node<K, V> s = new Node<K, V>(T);
				Root = s;
				s.Count = 0;
				s.children[0] = r;
				split(s, 0);
				insert(s, k, val);
			} else
				insert(r, k, val);
		}
	}

	/**
	 * Проверка если ли ключ в данном дереве
	 *
	 * @param key ключ
	 * @return true or false
	 */
	public boolean containsKey(K key) {
		if (search(Root, key) == null)
			return false;
		else
			return true;
	}

	/**
	 * Поиск в узле по ключу
	 *
	 * @param x узел
	 * @param k ключ
	 * @return пара ключ-значение
	 */
	private Entry<K, V> search(Node<K, V> x, K k) {
		int i = 0;
		while (i < x.Count && x.entry[i].less(k)) i++;
		if (i < x.Count && x.entry[i].eq(k))
			return x.entry[i];
		if (x.children[i] == null)
			return null;
		return search(x.children[i], k);
	}


	private void split(Node<K, V> x, int ind) {
		Node<K, V> z = new Node<K, V>(T);
		Node<K, V> y = x.children[ind];
		z.leaf = y.leaf;
		z.Count = T - 1;
		for (int i = 0; i < z.Count; i++)
			z.entry[i] = y.entry[i + T];
		if (!y.leaf)
			for (int i = 0; i < T; i++) {
				z.children[i] = y.children[i + T];
			}
		y.Count = T - 1;
		for (int i = x.Count; i > ind; i--)
			x.children[i + 1] = x.children[i];
		x.children[ind + 1] = z;
		for (int i = x.Count; i > ind; i--)
			x.entry[i] = x.entry[i - 1];
		x.entry[ind] = y.entry[T - 1];
		x.Count++;
	}

	/**
	 * Вставка значения V в узел X по ключу K
	 *
	 * @param x   Узел
	 * @param k   Ключ
	 * @param val Значение
	 */
	private void insert(Node x, K k, V val) {
		int i = x.Count;
		if (x.leaf) {
			for (int j = 0; j < i; j++)
				if (x.entry[j].eq(k))
					return;
			for (; i > 0 && x.entry[i - 1].larger(k); i--)
				x.entry[i] = x.entry[i - 1];
			x.entry[i] = new Entry(k, val);
			x.Count++;
		} else {
			while (i > 0 && x.entry[i - 1].larger(k)) i--;
			if (x.children[i].Count == 2 * T - 1) {
				split(x, i);
				if (x.entry[i].less(k))
					i++;
			}
			insert(x.children[i], k, val);
		}
	}

	/**
	 * Удаление ключа из дерева
	 *
	 * @param key удаляемый ключ
	 */
	public void del(K key) {
		if (search(Root, key) != null) {
			delete(Root, key);
			size--;
		}
	}

	/**
	 * Удаление максимального найденного ключа из заданного диапазона
	 *
	 * @param min нижняя граница для поиска элемента
	 * @param max верхняя граница для поиска элемента
	 * @return Удаленное значение
	 */
	public K maxOnSelect(K min, K max) {
		int i = 0;
		Entry<K, V> cur = null;
		Node<K, V> x = Root;
		while (x != null) {
			i = 0;
			while (i < x.Count && (x.entry[i].less(max) || x.entry[i].eq(max))) {
				if ((x.entry[i].larger(min) && x.entry[i].less(max)) || x.entry[i].eq(max) || x.entry[i].eq(min)) {
					if (cur == null || cur.less(x.entry[i].getKey())) {
						cur = x.entry[i];
					}
				}
				i++;
			}
			x = x.children[i];
		}
		if (cur == null)
			return null;
		return cur.getKey();
	}

	/**
	 * Удаление ключа из узла, при условии что он есть в этом узле, иначе рекурсивный спуск к узлу с ключом
	 *
	 * @param x   узел из которого нужно удалить ключ key
	 * @param key удаляемый ключ
	 */
	private void delete(Node<K, V> x, K key) {
		int Count = x.Count;
		int ind = -1;
		for (int i = 0; i < Count; i++)
			if (x.entry[i].eq(key))
				ind = i;
		if (x.leaf) {
			for (int i = ind + 1; i < Count; i++)
				x.entry[i - 1] = x.entry[i];
			x.Count--;
		} else {
			if (ind != -1) {
				if (x.children[ind].Count >= T)
					x.entry[ind] = x.children[ind].entry[x.children[ind].Count-- - 1];
				else if (ind <= Count && x.children[ind + 1].Count >= T) {
					x.entry[ind] = x.children[ind + 1].entry[0];
					for (int i = 1; i < x.children[ind + 1].Count; i++)
						x.children[ind + 1].entry[i - 1] = x.children[ind + 1].entry[i];
					x.children[ind + 1].Count--;
				} else if (x.children[ind].Count == T - 1 && ind <= Count && x.children[ind + 1].Count == T - 1) {
					Merge(x, ind);
					for (int i = 0; i < T - 1; i++)
						x.children[ind].entry[i + T - 1] = x.children[ind].entry[i + T];
					for (int i = 0; i < T; i++)
						x.children[ind].children[i + T - 1] = x.children[ind].children[i + T];
					x.children[ind].Count--;
				}
			} else {
				ind = 0;
				while (ind < Count && x.entry[ind].less(key)) ind++;
				if (x.children[ind].Count == T - 1)
					if (ind > 0 && x.children[ind - 1].Count == T)
						Transplant(x, ind - 1);
					else if (ind < Count && x.children[ind + 1].Count == T)
						Transplant(x, ind);
					else if (ind > 0 && x.children[ind - 1].Count == T - 1)
						Merge(x, ind-- - 1);
					else if (ind < Count && x.children[ind + 1].Count == T - 1)
						Merge(x, ind--);
				if (ind == -1)
					ind++;
				delete(x.children[ind], key);
			}
		}
	}


	private void Transplant(Node x, int i) {
		x.children[i].entry[T - 1] = x.entry[i];
		x.children[i].children[T] = x.children[i + 1].children[0];
		x.entry[i] = x.children[i + 1].entry[0];
		for (int j = 0; j < T; j++)
			x.children[i + 1].entry[j] = x.children[i + 1].entry[j + 1];
		for (int j = 0; j <= T; j++)
			x.children[i + 1].children[j] = x.children[i + 1].children[j + 1];
		x.children[i].Count++;
		x.children[i + 1].Count--;
	}


	private void Merge(Node x, int i) {
		x.children[i].entry[T - 1] = x.entry[i];
		for (int j = 0; j < T - 1; j++)
			x.children[i].entry[j + T] = x.children[i + 1].entry[j];
		for (int j = 0; j < T; j++)
			x.children[i].children[j + T] = x.children[i + 1].children[j];

		for (int j = i + 1; j < x.Count; j++)
			x.entry[j - 1] = x.entry[j];
		for (int j = i + 2; j <= x.Count; j++)
			x.children[j - 1] = x.children[j];

		x.Count--;
		x.children[i].Count = 2 * T - 1;
		if (Root == x && x.Count == 0) {
			Root = x.children[0];
		}
	}

	/**
	 * Обновление графического представлениея дерева.
	 */
	public void refreshXY() {
		if (Root != null) {
			refreshX(Root, sX);
			refreshY(Root, sY);
		}
	}

	/**
	 * расчет координаты X для элемента N
	 *
	 * @param n    Узел для которого нужно расчитать координату X
	 * @param curX координата стартовая X для узла
	 * @return координата X для следующего узла
	 */
	private double refreshX(Node n, double curX) {
		double s = curX;
		double min = s;

		for (int i = 0; i <= n.Count; i++) {
			if (n.children[i] != null)
				s = refreshX(n.children[i], s + distX);
		}
		//n.setX((min + s) / 2 - n.getWidth() / 2);
		n.setX((min + s) / 2);
		return s + n.getWidth();
	}

	/**
	 * расчет координаты Y для элемента N
	 *
	 * @param n    Узел для которого нужно расчитать координату Y
	 * @param curY координата стартовая Y для узла
	 * @return координата Y для следующего узла
	 */
	private void refreshY(Node n, double curY) {
		n.setY(curY);
		for (Node child : n.children)
			if (child != null)
				refreshY(child, curY + distY);
	}

	/**
	 * Плолучение графического представления дерева
	 *
	 * @return Массив фигур
	 */
	public ArrayList<Shape> getShapeTree() {
		if (Root != null)
			return getShapeNode(Root);
		return new ArrayList<>();
	}

	/**
	 * Плолучение графического представления узла
	 *
	 * @param t узел
	 * @return Массив фигур
	 */
	private ArrayList<Shape> getShapeNode(Node t) {
		ArrayList<Shape> res = new ArrayList<>();
		res.addAll(t.getShape());
		if (t.leaf)
			return res;
		for (int i = 0; i <= t.Count; i++) {
			if (t.children[i] != null)
				res.addAll(getShapeNode(t.children[i]));
		}
		return res;
	}
}