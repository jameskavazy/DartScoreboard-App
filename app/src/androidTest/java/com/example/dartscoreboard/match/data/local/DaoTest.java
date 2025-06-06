package com.example.dartscoreboard.match.data.local;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.dartscoreboard.LiveDataTestUtil;
import com.example.dartscoreboard.application.data.local.Database;
import com.example.dartscoreboard.match.data.models.Leg;
import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchSettings;
import com.example.dartscoreboard.match.data.models.MatchType;
import com.example.dartscoreboard.match.data.models.MatchUsers;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.match.data.models.User;
import com.example.dartscoreboard.match.data.models.Visit;
import com.example.dartscoreboard.match.models.Statistics;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Completable;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DaoTest {
    private UserDao userDao;
    private StatsDao statsDao;
    private MatchDao matchDao;
    private Database db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room
                .inMemoryDatabaseBuilder(context, Database.class)
                .setTransactionExecutor(Executors.newSingleThreadExecutor())
                .build();
        matchDao = db.matchesDao();
        statsDao = db.statsDao();
        userDao = db.userDao();


    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void testInsertUser() throws Exception {
        User user = new User("TEST");
        userDao.insertUser(user);
        List<User> users = LiveDataTestUtil.getOrAwaitValue(userDao.getAllUsers());
        //see also RxJava test?
        assert users != null;
        assertEquals("user size is 1", 1, users.size());
    }

    @Test
    public void testDeleteUser() throws InterruptedException {

        User user = new User("TEST");
        userDao.insertUser(user);

        User user2 = new User("TEST2");
        user2.userId = 2;
        userDao.insertUser(user2);


        userDao.deleteUser(user2);
        List<User> users = LiveDataTestUtil.getOrAwaitValue(userDao.getAllUsers());
//        Handler handler = new Handler(Looper.getMainLooper());
//        List<User> users = new ArrayList<>();
//        handler.post(() -> {
//            try {
//                users.addAll(LiveDataTestUtil.getOrAwaitValue(userDao.getAllUsers()));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
        assertEquals("user size", 1, users.size());
    }

    @Test
    public void testInsertMatch() throws InterruptedException {
        Match match = createDummyMatch();
        matchDao.insertMatch(match);
        List<Match> matches = LiveDataTestUtil.getOrAwaitValue(matchDao.getAllMatchHistory());
        assertEquals("testMatchId", matches.get(0).matchId);
    }

    @Test
    public void testInsertSet() {
        Match match = createDummyMatch();
        matchDao.insertMatch(match);

        Set set = new Set("testSetId", match.matchId);
        matchDao.insertSet(set);

        String got = matchDao.getLatestSetId(match.matchId).blockingGet();
        String want = set.setId;
        assertEquals(got, want);
    }

    @Test
    public void testInsertLeg(){
        Match match = createDummyMatch();
        matchDao.insertMatch(match);

        Set set = new Set("testSetId", match.matchId);
        matchDao.insertSet(set);

        Leg leg = new Leg("testLegId", set.setId, match.matchId,0);
        matchDao.insertLeg(leg);

        String got = matchDao.getLatestLegId(match.matchId).blockingGet();
        String want = "testLegId";

        assertEquals(got, want);
    }


    @Test
    public void testInsertVisit() throws InterruptedException {
        User user = new User("testPLayer");
        userDao.insertUser(user);

        Match match = createDummyMatch();
        matchDao.insertMatch(match);

        Set set = new Set("testSetId", match.matchId);
        matchDao.insertSet(set);

        Leg leg = new Leg("testLegId", set.setId, match.matchId,0);
        matchDao.insertLeg(leg);

        Visit visit = new Visit();

        visit.userId = 1;
        visit.score = 22;
        visit.legId = leg.legId;
        matchDao.insertVisit(visit);

        long got = matchDao.getLatestLegWithVisits(match.matchId).blockingFirst().visits.get(0).visitId;
        long want = 1;

        assertEquals(got, want);
    }


    @Test
    public void testWinsStat() throws Throwable {
        // todo insert match won, ask statsdao the question, check answer...
        createValidMatchWithWinner("testMatchId", "testSetId", "testLegId", 1, 1,1, new int[]{170});
        Statistics stats = statsDao.getUserStats(1).blockingGet();

        int got = stats.getWins();
        int want = 1;

        assertEquals(want, got);
    }

    @Test
    public void testLossesStat() throws Throwable {
        createValidMatchWithWinner("testMatchId","testSetId", "testLegId",1, 1,1, new int[]{170});
        Statistics statistics = statsDao.getUserStats(2).blockingGet();

        int got = statistics.getLosses();
        int want = 1;

        assertEquals(want, got);
    }

    @Test
    public void testAvg() throws Throwable {
        createValidMatchWithWinner("testMatchId","testSetId", "testLegId",1,1,1,  new int[]{170, 55, 20});

        Statistics statistics = statsDao.getUserStats(1).blockingGet();

        double got = statistics.getAverageScore();
        double want = 81.6;
        

        assertEquals(want, got, 0.09999);
    }


    @Test
    public void testWinRate() throws Throwable {
        createValidMatchWithWinner("testMatchId","testSetId", "testLegId",1,1,1,  new int[]{150, 20, 44, 45, 27});
        createValidMatchWithWinner("testMatchId2","testSetId2", "testLegId2",1,1,1,  new int[]{48, 24, 39, 45, 26});
        createValidMatchWithWinner("testMatchId3","testSetId3", "testLegId3",2, 1,1, new int[]{44, 20, 76, 12, 27});

        Statistics statistics = statsDao.getUserStats(1).blockingGet();

        int got = statistics.getMatchWinRate();
        int want = 66;

        assertEquals(want, got);

    }

    @Test
    public void testMatchesPlayed() throws Throwable {
        createValidMatchWithWinner("testMatchId","testSetId", "testLegId",1,1,1,  new int[]{155, 60, 20});
        Statistics statistics = statsDao.getUserStats(1).blockingGet();
        int got = statistics.getMatchesPlayed();
        int want = 1;

        assertEquals(want, got);
    }

    @Test
    public void testMatchesPlayedIncludingPracticeMatch() throws Throwable {
        createValidMatchWithWinner("testMatchId","testSetId", "testLegId",1,1,1,  new int[]{150, 20});
        createSinglePlayerPracticeMatch(1,new int[]{150, 20, 50});

        Statistics statistics = statsDao.getUserStats(1).blockingGet();
        int got = statistics.getMatchesPlayed();
        int want = 1;

        assertEquals(want, got);
    }

    @Test
    public void testLegsWon() throws Throwable {
        createValidMatchWithWinner("testMatchId", "testSetId", "testLegId", 1,1,1,  new int[]{12, 44, 120});
        Statistics statistics = statsDao.getUserStats(1).blockingGet();
        int got = statistics.getLegsWon();
        int want = 1;
        assertEquals(got, want);
    }

    @Test
    public void testCheckoutRate() throws Throwable {
        int userId1 = insertUserAndGetId("player 1");
        int userId2 = insertUserAndGetId("player 2");

        Match match = new Match("testMatchId", MatchType.FiveO, new MatchSettings(1,1), OffsetDateTime.now());
        match.winnerId = 1;
        matchDao.insertMatch(match);

        MatchUsers matchUsers = new MatchUsers(userId1, match.matchId, 0);
        userDao.insertToGame(matchUsers);

        MatchUsers matchUsers2 = new MatchUsers(userId2, match.matchId, 1);
        userDao.insertToGame(matchUsers2);

        Set set = new Set("testSetId", match.matchId);
        set.winnerId = 1;
        matchDao.insertSet(set);

        Leg leg = new Leg("testLegId", set.setId, match.matchId,0);
        leg.winnerId = 1;
        matchDao.insertLeg(leg);

        generateVisits(new int[]{55, 25, 70}, leg, userId1);
        generateVisits(new int[]{55, 25, 70}, leg, userId2);

        createAndInsertVisit(leg, 1, 0, true);
        createAndInsertVisit(leg, 1, 1, true);
        createAndInsertVisit(leg, 1, 9, true);
        createAndInsertVisit(leg, 1, 10, true);


        Statistics statistics = statsDao.getUserStats(1).blockingGet();

        int got = statistics.getCheckoutRate();
        int want = 25;
        assertEquals(want, got);
    }

    @Test
    public void testLegWinRate() throws Throwable {
        createValidMatchWithWinner("matchIdTest", "setIdTest", "legIdTest", 1, 1, 1, new int[]{1, 2, 44, 129});
        createValidMatchWithWinner("matchIdTest2", "setIdTest2", "legIdTest2", 1, 1, 2, new int[]{1, 2, 44, 129});
        createValidMatchWithWinner("matchIdTest3", "setIdTest3", "legIdTest3", 1,1, 2, new int[]{1, 2, 44, 129});
        createValidMatchWithWinner("matchIdTest4", "setIdTest4", "legIdTest4", 1,1, 2, new int[]{1, 2, 44, 129});
        createValidMatchWithWinner("matchIdTest5", "setIdTest5", "legIdTest5", 1, 1, 1,new int[]{1, 2, 44, 129});

        Statistics statistics = statsDao.getUserStats(1).blockingGet();

        int got = statistics.getLegWinRate();
        int want = 40;

        assertEquals(want, got);
    }

    @Test
    public void testSegments() throws Throwable {
        createValidMatchWithWinner("matchIdTest", "setIdTest", "legIdTest", 1, 1, 1,
                new int[]{1, 2, 44, 129, 66, 98, 104, 180, 180, 180, 141}
        );

        checkDb(
                "SELECT * FROM match_stats_view ms" +
                        " JOIN visit_stats_view vs ON vs.matchId = ms.matchId" +
                        " JOIN leg_stats_view ls ON ls.matchId = ms.matchId" +
                        " WHERE ms.userId = 1"
        );

        Statistics statistics = statsDao.getUserStats(1).blockingGet();

        int gotSegmentBelow60 = statistics.getSegmentBelow60();
        int wantSegmentBelow60 = 3;

        int gotSegment60To99 = statistics.getSegment60To99();
        int wantSegment60To99 = 2;

        int gotSegment100To139 = statistics.getSegment100To139();
        int wantSegment100To139 = 2;

        int gotSegment140To179 = statistics.getSegment140To179();
        int wantSegment140To179 = 1;

        int gotSegment180 = statistics.getSegment180();
        int wantSegment180 = 3;



        assertEquals("Segment Below 60", wantSegmentBelow60, gotSegmentBelow60);
        assertEquals("Segment 60 to 99", wantSegment60To99, gotSegment60To99);
        assertEquals("Segment 100 to 139", wantSegment100To139, gotSegment100To139);
        assertEquals("Segment 140 to 179", wantSegment140To179, gotSegment140To179);
        assertEquals("Segment 180", wantSegment180, gotSegment180);

    }

    @NonNull
    private Match createDummyMatch() {
        return new Match("testMatchId", MatchType.FiveO, new MatchSettings(1,1), OffsetDateTime.now());
    }

    private String createValidMatchWithWinner(
            String matchId,
            String setId,
            String legId,
            int matchWinner,
            int setWinner,
            int legWinner,
            int[] visitScores
    ) throws Throwable {
        int userId1 = insertUserAndGetId("player 1");
        int userId2 = insertUserAndGetId("player 2");

        Match match = new Match(matchId, MatchType.FiveO, new MatchSettings(1,1), OffsetDateTime.now());
        match.winnerId = matchWinner;
        matchDao.insertMatch(match);

        MatchUsers matchUsers = new MatchUsers(userId1, match.matchId, 0);
        userDao.insertToGame(matchUsers);

        MatchUsers matchUsers2 = new MatchUsers(userId2, match.matchId, 1);
        userDao.insertToGame(matchUsers2);

        Set set = new Set(setId, match.matchId);
        set.winnerId = setWinner;
        matchDao.insertSet(set);

        Leg leg = new Leg(legId, set.setId, match.matchId,0);
        leg.winnerId = legWinner;
        matchDao.insertLeg(leg);

        generateVisits(visitScores, leg, userId1);
        generateVisits(visitScores, leg, userId2);

        return match.matchId;
    }

    private int insertUserAndGetId(String username) throws Throwable {
        User user = new User(username);
        userDao.insertUser(user);
        return LiveDataTestUtil.getOrAwaitValue(userDao.getAllUsers())
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> null)
                .userId;
    }

    private void createSinglePlayerPracticeMatch(int userId, int[] scores){
        String matchId = "practiceMatchId";
        Match match = new Match(matchId, MatchType.FiveO, new MatchSettings(1, 1), OffsetDateTime.now());
        match.winnerId = userId;
        matchDao.insertMatch(match);

        Set set = new Set("set-" + matchId, matchId);
        matchDao.insertSet(set);


        Leg leg = new Leg("leg-" + matchId, "set-" + matchId, matchId, 0);
        matchDao.insertLeg(leg);

        generateVisits(scores, leg, userId);
    }

    private void generateVisits(int[] visitScores, Leg leg, int userId) {
        for (int score : visitScores) {
            Visit visit = new Visit();
            visit.userId = userId;
            visit.score = score;
            visit.legId = leg.legId;
            matchDao.insertVisit(visit);
        }
    }

    private void createAndInsertVisit(Leg leg, int userId, int score, boolean checkout) {
        Visit visit = new Visit();
        visit.userId = userId;
        visit.score = score;
        visit.legId = leg.legId;
        visit.checkout = checkout;
        matchDao.insertVisit(visit);
    }


    private void checkDb(String queryString) {
        Cursor cursor = db.query(new SimpleSQLiteQuery(queryString));
        StringBuilder rowOutput = new StringBuilder();
        int columnCount = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            rowOutput.setLength(0); // Clear builder
            for (int i = 0; i < columnCount; i++) {
                String columnName = cursor.getColumnName(i);
                String value = cursor.isNull(i) ? "NULL" : cursor.getString(i);
                rowOutput.append(columnName).append(": ").append(value).append(" | ");
            }
            Log.d("VISIT_STATS_VIEW", rowOutput.toString());
        }
        cursor.close();
    }

//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
////        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
////        assertEquals("com.example.dartscoreboard", appContext.getPackageName());
//
//
//
//
//    }
}