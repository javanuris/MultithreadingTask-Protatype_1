package service;

import ship.AbstractShip;
import ship.Creator;
import ship.ShipCreator;
import thread.CommonShipThread;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by User on 05.03.2017.
 */
public class ExecuteThread {

    private CopyOnWriteArrayList<AbstractShip> comeShipList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<AbstractShip> leaveShipList = new CopyOnWriteArrayList<>();

    private ArrayList<Future<AbstractShip>> result = new ArrayList<>();

    private ExecutorService oilExec = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT),
            eatExec = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT),
            boxExec = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT);

    public ExecuteThread() {
        executeTask();
        resultExecute();
        endExecuter();
    }

    private void executeTask() {
        for (int i = 0; i < 10; i++) {
            comeShipList.add(new ShipCreator(Creator.TYPE_SHIP[new Random().nextInt(3)], Creator.TYPE_CAPACITY[new Random().nextInt(3)]));
            comeShipList.get(i).setId(i + 1);
        }
        for (AbstractShip ship : comeShipList) {
            switch (ship.getType()) {
                case Creator.EAT_SHIP:
                    result.add(eatExec.submit(new CommonShipThread(ship)));
                    break;
                case Creator.BOX_SHIP:
                    result.add(boxExec.submit(new CommonShipThread(ship)));
                    break;
                case Creator.OIL_SHIP:
                    result.add(oilExec.submit(new CommonShipThread(ship)));
                    break;
                default:
                    break;
            }
        }
    }

    private void resultExecute() {
        for (Future<AbstractShip> fs : result) {
            try {
                ///   System.out.println(fs.get().toString());
                leaveShipList.add(fs.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error to adding a varible into list");
            }
        }
        System.out.println("Итог: ");
        for (AbstractShip abstractShip : leaveShipList) {
            System.out.println(abstractShip.toString());
        }
    }

    private void endExecuter() {
        oilExec.shutdown();
        boxExec.shutdown();
        eatExec.shutdown();
    }
}
