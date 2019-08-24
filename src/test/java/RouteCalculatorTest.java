import core.Line;
import core.Station;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class RouteCalculatorTest extends TestCase
{
    List<Station> route; //маршрут для проверки продолжительности пути по сложному маршруту (2 пересадки + движение по линии)
    List<Station> routeOnTheLine; //маршрут на одной линии
    List<Station> routeWithOneConnection; //маршрут с одной пересадкой
    List<Station> routeWithTwoConnections; //маршрут с двумя пересадками
    List<Station> routeToSameStation; //маршрут в ту же станцию
    List<Station> routeToOppositeStationsNextToEachOther; // маршрут движения внутри узла (переход между станциями)
    RouteCalculator calculator;
    StationIndex stationIndex;
    List<Station> connectionStations1; // Пересадочный узел №1
    List<Station> connectionStations2; // Пересадочный узел №2

    //Станции
    Station station1;
    Station station2;
    Station station3;
    Station station4;
    Station station5;
    Station station6;

    //Линии метро
    Line line1;
    Line line2;
    Line line3;

    @Override
    protected void setUp() throws Exception {
        route = new ArrayList<>();
        routeOnTheLine = new ArrayList<>();
        routeWithOneConnection = new ArrayList<>();
        routeWithTwoConnections = new ArrayList<>();
        routeToSameStation = new ArrayList<>();
        routeToOppositeStationsNextToEachOther = new ArrayList<>();
        connectionStations1 = new ArrayList<>();
        connectionStations2 = new ArrayList<>();

        line1 = new Line(1, "Первая");
        line2 = new Line(2, "Вторая");
        line3 = new Line(3,"Третья");

        //станции первой линии
        station1 = new Station("Петровская", line1);
        station2 = new Station("Арбузная", line1);

        //станции второй линии
        station3 = new Station("Морковная", line2);
        station4 = new Station("Яблочная", line2);

        //станции третьей линии
        station5 = new Station("Томатная", line3);
        station6 = new Station("Банановая", line3);

        //добавляем станции в линии
        line1.addStation(station1);
        line1.addStation(station2);
        line2.addStation(station3);
        line2.addStation(station4);
        line3.addStation(station5);
        line3.addStation(station6);

        //создаем списки пересадочных узлов
        connectionStations1.add(station2);
        connectionStations1.add(station3);
        connectionStations2.add(station4);
        connectionStations2.add(station5);

        route.add(station1);
        route.add(station2);
        route.add(station3);
        route.add(station4);
        route.add(station5);
        route.add(station6);

        routeOnTheLine.add(station1);
        routeOnTheLine.add(station2);

        routeWithOneConnection.add(station1);
        routeWithOneConnection.add(station2);
        routeWithOneConnection.add(station3);
        routeWithOneConnection.add(station4);

        routeWithTwoConnections.add(station1);
        routeWithTwoConnections.add(station2);
        routeWithTwoConnections.add(station3);
        routeWithTwoConnections.add(station4);
        routeWithTwoConnections.add(station5);
        routeWithTwoConnections.add(station6);

        routeToSameStation.add(station5);

        routeToOppositeStationsNextToEachOther.add(station2); //Добавляем узловые станции
        routeToOppositeStationsNextToEachOther.add(station3);

        stationIndex = new StationIndex();
        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);

        stationIndex.addStation(station1);
        stationIndex.addStation(station2);
        stationIndex.addStation(station3);
        stationIndex.addStation(station4);
        stationIndex.addStation(station5);
        stationIndex.addStation(station6);

        stationIndex.addConnection(connectionStations1);
        stationIndex.addConnection(connectionStations2);

        calculator = new RouteCalculator(stationIndex);

    }

    //Проверка длительности сложного маршрута (2 пересадки + движение по линии)
    public void testCalculateDuration__Complex_route_with_one_connection()
    {
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 14.5;
        assertEquals(expected, actual);
    }

    //Проверка составления маршрута на одной линии
    public void testGetShortestRoute_Get_route_on_the_line()
    {
        List<Station> routeOnTheLineActual = calculator.getShortestRoute(station1, station2);
        assertEquals(routeOnTheLine, routeOnTheLineActual);
    }

    //Проверка длительности маршрута на одной линии соседних станций
    public void testCalculateDuration_Stations_next_to_each_other_on_single_line()
    {
        double actual = RouteCalculator.calculateDuration(routeOnTheLine);
        double expected = 2.5;
        assertEquals(expected,actual);
    }

    //Проверка составления маршрута с одной пересадкой
    public void testGetShortestRoute_Get_route_with_one_connection()
    {
        List<Station> routeWithOneConnectionActual = calculator.getShortestRoute(station1, station4);
        assertEquals(routeWithOneConnection, routeWithOneConnectionActual);
    }

    //Проверка составления маршрута с двумя пересадками
    public void testGetShortestRoute_Get_route_with_two_connections()
    {
        List<Station> routeWithTwoConnectionsActual = calculator.getShortestRoute(station1, station6);
        assertEquals(routeWithTwoConnections, routeWithTwoConnectionsActual);
    }

    //Проверка длительности маршрута внутри узла (переход между станциями)
    public void testCalculateDuration_To_opposite_stations_next_to_each_other()
    {
        double actual = RouteCalculator.calculateDuration(routeToOppositeStationsNextToEachOther);
        double expected = 3.5;
        assertEquals(expected,actual);
    }

    //Проверка составления маршрута в ту же станцию
    public void testGetShortestRoute_Get_route_to_same_station()
    {
        List<Station> routeToSameStationActual = calculator.getShortestRoute(station5, station5);
        assertEquals(routeToSameStation, routeToSameStationActual);
    }

    //Проверка длительности маршрута в рамках одной станции
    public void testCalculateDuration_To_same_station()
    {
        double actual = RouteCalculator.calculateDuration(routeToSameStation);
        double expected = 0.0;
        assertEquals(expected,actual);
    }







}

