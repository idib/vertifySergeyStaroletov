package com.verify;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * \brief Основной класс проекта
 * \author Artem Fast
 * \version 1.0
 * <p>
 * Класс, реализующий хранение множества точек и быстрый поиск ближайших точек в пределах заданного радиуса
 */
public class NearestPoints {

    /**
     * \brief Вспомогательный класс
     * \author Artem Fast
     * \version 1.0
     * <p>
     * Класс, реализующий хранение двух целочисленных координат
     */
    protected class Position {
        public int i, j;

        /**
         Базовый конструктор класса
         */
        public Position(int i, int j) {
            this.i = i;
            this.j = j;
        }

        /**
         Перегрузка метода сравнения
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;
            Position position = (Position) o;
            return i == position.i &&
                    j == position.j;
        }
        /**
         Перегрузка метода вычисления хэша
         */
        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + j;
            return result;
        }
    }

    /// деревья, хранящие сохраненные точки
    protected HashMap<Long, Point>[][] map;

    /// хранит, в каком дереве сейчас лежит текущий элемент
    protected HashMap<Long, Position> positions;

    /// размерность разбиения координат
    protected final int N = 400;

    /// радиус, в котором будем производить поиск
    protected final int radius;

    /// конструктор по умолчанию
    public NearestPoints() {
        this(100); //default radius
    }

    @SuppressWarnings("unchecked")
    /**
     Базовый конструктор класса
     \param[in] radius радиус, в котором будем производить поиск
     */
    public NearestPoints(int radius) {
        this.radius = radius;
        map = new HashMap[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                map[i][j] = new HashMap<>();
            }
        }
        positions = new HashMap<>();
    }

    /**
     Добавление точки
     \param[in] id уникальный id точки
     \param[in] x x-координата точки
     \param[in] y y-координата точки
     */
    public boolean addPoint(Long id, double x, double y) {
        int i, j;
        try {
            i = getMapCoordinate(x);
            j = getMapCoordinate(y);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        map[i][j].put(id, new Point(new DegreeCoordinate(x), new DegreeCoordinate(y)));
        positions.put(id, new Position(i, j));
        return true;
    }

    /**
     Изменение координат точки
     \param[in] id уникальный id точки
     \param[in] x x-координата точки
     \param[in] y y-координата точки
     */
    public boolean movePoint(Long id, double x, double y) {
        Position currentPosition = positions.get(id);
        if (currentPosition == null) {
            return addPoint(id, x, y);
        }
        int i, j;
        try {
            i = getMapCoordinate(x);
            j = getMapCoordinate(y);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        Position newPosition = new Position(i, j);
        Point newPoint = new Point(new DegreeCoordinate(x), new DegreeCoordinate(y));

        if (currentPosition.equals(newPosition)) {
            map[i][j].replace(id, newPoint);
        } else {
            map[currentPosition.i][currentPosition.j].remove(id);
            map[i][j].put(id, newPoint);
            positions.replace(id, newPosition);
        }
        return true;
    }

    /**
     Удаление точки
     \param[in] id уникальный id точки
     */
    public boolean deletePoint(Long id) {
        Position currentPosition = positions.remove(id);
        if (currentPosition != null) {
            map[currentPosition.i][currentPosition.j].remove(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     Поиск всех ближайших точек в пределах заданного радиуса
     \param[in] id уникальный id точки
     */
    public JsonArray getClosestPoints(Long id) {
        JsonArray answer = new JsonArray();
        Position currentPosition = positions.get(id);
        if (currentPosition == null) {
            return answer;
        }
        Point point = map[currentPosition.i][currentPosition.j].get(id);
        BoundingArea area = EarthCalc.getBoundingArea(point, radius);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int ni = currentPosition.i + i;
                int nj = currentPosition.j + j;
                for (Map.Entry<Long, Point> entry : map[ni][nj].entrySet()) {
                    Long neighbourId = entry.getKey();
                    if (id.equals(neighbourId)) {
                        continue;
                    }
                    Point p = entry.getValue();
                    if (area.isContainedWithin(p)) {
                        answer.add(new JsonObject()
                                .put("id", neighbourId)
                                .put("lat", p.getLatitude())
                                .put("lon", p.getLongitude()));
                    }
                }
            }
        }
        return answer;
    }

    // Возвращает позицию в массиве хэшмапов
    protected int getMapCoordinate(double value) throws IndexOutOfBoundsException {
        if (!checkDegrees(value))
            throw new IndexOutOfBoundsException("Degree value " + value + " out of bounds");
        value += 190; // гарантировано положительное
        return (int) value;
    }

    // Грубая проверка, что координата в градусах лежит в пределах [-180, 180]
    public boolean checkDegrees(double value) {
        return !(value < -180 - 0.01 || value > 180 + 0.01);
    }
}
