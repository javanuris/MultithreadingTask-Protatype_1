package main;


import ship.AbstractShip;
import ship.ShipCreator;
import ship.Creator;
import thread.CommonShipThread;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by User on 04.03.2017.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<AbstractShip> leaveShipList = new ArrayList<>();
        ArrayList<AbstractShip> comeShipList = new ArrayList<>();

        ExecutorService oilExec = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT);
        ExecutorService eatExec = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT);
        ExecutorService boxExec = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT);

        ArrayList<Future<AbstractShip>> result = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            comeShipList.add(new ShipCreator(Creator.TYPE_SHIP[new Random().nextInt(3)], Creator.TYPE_CAPACITY[new Random().nextInt(3)]));
            comeShipList.get(i).setId(i+1);
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

        for (Future<AbstractShip> fs : result) {
            try {
                leaveShipList.add(fs.get());
            } catch (Exception e) {

            }
        }
        System.out.println("Итог: ");
        for (AbstractShip abstractShip : leaveShipList) {
            System.out.println(abstractShip.toString());
        }
        oilExec.shutdown();
        boxExec.shutdown();
        eatExec.shutdown();
    }
}
