package com.BTree;

/**
 *	Класс обстракного ребенка в дереве.
 */
public abstract class AbstractChildren {
	/**
	 *	Длина стрелки.
	 */
	static double lengPointLint = 10;
	/**
	 *	Внутренний отступ.
	 */
	static double padding = 5;
	/**
	 *	координаты x,y
	 *	ширина и высота, элемента GUI
	 */
    protected double x, y, width,height;

	/**
	 *
	 * @return Длина стрелки.
	 */
    public static double getLengPointLint() {
        return lengPointLint;
    }

	/**
	 *
	 * @param lengPointLint Задать длину стрелки
	 */
	static void setLengPointLint(double lengPointLint) {
        AbstractChildren.lengPointLint = lengPointLint;
    }

	/**
	 *
	 * @return Внутренний отступ.
	 */
	public static double getPadding() {
        return padding;
    }

	/**
	 *
	 * @param padding Задать вутренний отступ.
	 */
	static void setPadding(double padding) {
        AbstractChildren.padding = padding;
    }

	/**
	 *
	 * @return x
	 */
	public double getX() {
        return x;
    }

	/**
	 *
	 * @param x координата x
	 */
	void setX(double x) {
        this.x = x;
    }

	/**
	 *
	 * @return y
	 */
    public double getY() {
        return y;
    }

	/**
	 *
	 * @param y координата y
	 */
    void setY(double y) {
        this.y = y;
    }

	/**
	 *
	 * @return width
	 */
    public double getWidth() {
        return width;
    }

	/**
	 *
	 * @param width ширина элемента GUI
	 */
    void setWidth(double width) {
        this.width = width;
    }

	/**
	 *
	 * @return height
	 */
    public double getHeight() {
        return height;
    }

	/**
	 *
	 * @param height высота элемента GUI
	 */
    void setHeight(double height) {
        this.height = height;
    }
}
