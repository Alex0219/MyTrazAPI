package de.fileinputstream.none.api.rank;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.user.MyTrazUser;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


public class RankManager {


    public static HashMap<String, String> ranks = new HashMap(); //First is UUID, second is Rank


    public static String getRank(String uuid, final Consumer<String> consumer) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final BasicDBObject found = (BasicDBObject) Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("uuid", uuid)).next();
                String rank = "";
                if (found == null) {

                } else {
                    rank = found.getString("rank");
                }
                consumer.accept(rank);
                executorService.shutdown();
            }
        });


        return null;
    }

    public static String getRank(String uuid) {
        final BasicDBObject found = (BasicDBObject) Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("uuid", uuid)).next();
        String rank = "";
        if (found == null) {

        } else {
            rank = found.getString("rank");
            return rank;
        }
        return "";
    }


    public static void setRank(String uuid, String rank) {

        MyTrazUser user = new MyTrazUser(UUIDFetcher.getName(UUID.fromString(uuid)));
        user.getDocument(new Consumer<BasicDBObject>() {
            @Override
            public void accept(BasicDBObject basicDBObject) {
                DBObject before = new BasicDBObject("rank", getRank(uuid));
                user.getDocument(new Consumer<BasicDBObject>() {
                    @Override
                    public void accept(BasicDBObject basicDBObject) {
                        BasicDBObject after = basicDBObject.append("rank", rank);
                        Bootstrap.getMongoManager().getPlayers().update(before, after);
                        System.out.println("Backend -> Updated user rank for: " + uuid + " to rank: " + rank);
                    }
                });


            }
        });
    }


}
