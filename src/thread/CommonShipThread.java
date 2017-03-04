package thread;
import ship.AbstractShip;
import java.util.concurrent.Callable;


/**
 * Created by User on 04.03.2017.
 */
public class CommonShipThread implements Callable<AbstractShip> {
    AbstractShip abstractShip;

    public CommonShipThread(AbstractShip abstractShip) {
        this.abstractShip = abstractShip;
    }

    @Override
    public AbstractShip call() throws Exception {
        System.out.println(abstractShip.toString() + " -> прибыл");
        Thread.sleep(1000);
        System.out.println("Номер: "+abstractShip.getId() + " начал загрузку");
        while (!abstractShip.loadDetermine()) {
            abstractShip.setGoodOnShip(abstractShip.getGoodOnShip() + 10);
            Thread.sleep(100);
        }
        System.out.println("Номер: "+abstractShip.getId() + " загрузился!");
        Thread.sleep(1000);
        System.out.println("Номер: "+abstractShip.getId() + " уплыл...");
        return abstractShip;
    }
}
