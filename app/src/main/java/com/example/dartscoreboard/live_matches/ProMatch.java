package com.example.dartscoreboard.live_matches;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "pro_match_cache")
public class ProMatch {

    @PrimaryKey
    private int id;
    private String name;
    private String status;
    private int duration;
    private int league_id;
    private int season_id;
    private String start_time;
    private String league_name;
    private String season_name;
    private int away_team_id;
    private int home_team_id;
    private String status_reason;
    private int tournament_id;
    private String away_team_name;
    private String home_team_name;
    private int away_team_score;
    private int home_team_score;
    private String tournament_name;
    private String league_hash_image;
    private String away_team_hash_image;
    private String home_team_hash_image;
    private int tournament_importance;
    private int away_team_period_1_score;
    private int home_team_period_1_score;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public int getDuration() {
        return duration;
    }

    public int getLeague_id() {
        return league_id;
    }

    public int getSeason_id() {
        return season_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getLeague_name() {
        return league_name;
    }

    public String getSeason_name() {
        return season_name;
    }

    public int getAway_team_id() {
        return away_team_id;
    }

    public int getHome_team_id() {
        return home_team_id;
    }

    public String getStatus_reason() {
        return status_reason;
    }

    public int getTournament_id() {
        return tournament_id;
    }

    public String getAway_team_name() {
        return away_team_name;
    }

    public String getHome_team_name() {
        return home_team_name;
    }

    public int getAway_team_score() {
        return away_team_score;
    }

    public int getHome_team_score() {
        return home_team_score;
    }

    public String getTournament_name() {
        return tournament_name;
    }

    public String getLeague_hash_image() {
        return league_hash_image;
    }

    public String getAway_team_hash_image() {
        return away_team_hash_image;
    }

    public String getHome_team_hash_image() {
        return home_team_hash_image;
    }

    public int getTournament_importance() {
        return tournament_importance;
    }

    public int getAway_team_period_1_score() {
        return away_team_period_1_score;
    }

    public int getHome_team_period_1_score() {
        return home_team_period_1_score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLeague_id(int league_id) {
        this.league_id = league_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setLeague_name(String league_name) {
        this.league_name = league_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
    }

    public void setAway_team_id(int away_team_id) {
        this.away_team_id = away_team_id;
    }

    public void setHome_team_id(int home_team_id) {
        this.home_team_id = home_team_id;
    }

    public void setStatus_reason(String status_reason) {
        this.status_reason = status_reason;
    }

    public void setTournament_id(int tournament_id) {
        this.tournament_id = tournament_id;
    }

    public void setAway_team_name(String away_team_name) {
        this.away_team_name = away_team_name;
    }

    public void setHome_team_name(String home_team_name) {
        this.home_team_name = home_team_name;
    }

    public void setAway_team_score(int away_team_score) {
        this.away_team_score = away_team_score;
    }

    public void setHome_team_score(int home_team_score) {
        this.home_team_score = home_team_score;
    }

    public void setTournament_name(String tournament_name) {
        this.tournament_name = tournament_name;
    }

    public void setLeague_hash_image(String league_hash_image) {
        this.league_hash_image = league_hash_image;
    }

    public void setAway_team_hash_image(String away_team_hash_image) {
        this.away_team_hash_image = away_team_hash_image;
    }

    public void setHome_team_hash_image(String home_team_hash_image) {
        this.home_team_hash_image = home_team_hash_image;
    }

    public void setTournament_importance(int tournament_importance) {
        this.tournament_importance = tournament_importance;
    }

    public void setAway_team_period_1_score(int away_team_period_1_score) {
        this.away_team_period_1_score = away_team_period_1_score;
    }

    public void setHome_team_period_1_score(int home_team_period_1_score) {
        this.home_team_period_1_score = home_team_period_1_score;
    }
}


/*
PDC World Championship: tournament id  = 31552, 63986



 */


//
//[
//        {
//        "date": "2024-03-03",
//        "matches": [
//        {
//        "id": 40468,
//        "name": "Lukeman M. vs Van Den Bergh D.",
//        "status": "upcoming",
//        "duration": 9000,
//        "league_id": 4001,
//        "season_id": 42207,
//        "start_time": "2024-03-03T16:00:00+00:00",
//        "league_name": "UK Open",
//        "season_name": "UK Open 2024",
//        "away_team_id": 47457,
//        "home_team_id": 82264,
//        "status_reason": "Not started",
//        "tournament_id": 77173,
//        "away_team_name": "Van Den Bergh D.",
//        "home_team_name": "Lukeman M.",
//        "tournament_name": "UK Open 2024",
//        "league_hash_image": "4954f966f6085b1f0f1466c57449b9d0010368f053869079beb3ce77a381780c",
//        "away_team_hash_image": "f11e98bf5cee8c1b35709a04172626fd1880098344f17282db20bdb882760b63",
//        "home_team_hash_image": "24d397748b07a1473105d13954d7ac267bf3414e2b7f30efcdabc21fe862b582",
//        "tournament_importance": 0
//        },
//        {
//        "id": 40469,
//        "name": "Bunting S. vs Humphries L.",
//        "status": "upcoming",
//        "duration": 9000,
//        "league_id": 4001,
//        "season_id": 42207,
//        "start_time": "2024-03-03T13:00:00+00:00",
//        "league_name": "UK Open",
//        "season_name": "UK Open 2024",
//        "away_team_id": 2324,
//        "home_team_id": 47494,
//        "status_reason": "Not started",
//        "tournament_id": 77173,
//        "away_team_name": "Humphries L.",
//        "home_team_name": "Bunting S.",
//        "tournament_name": "UK Open 2024",
//        "league_hash_image": "4954f966f6085b1f0f1466c57449b9d0010368f053869079beb3ce77a381780c",
//        "away_team_hash_image": "95f2b782d8a55d2a23da4c9b981242f84eb2c12d862e0fb04757ca85c4c36424",
//        "home_team_hash_image": "0bdcb2f74425a1e70b028e51b96a476924a54a740eee9ed549b42333aaf9cd67",
//        "tournament_importance": 0
//        },
//        {
//        "id": 40470,
//        "name": "Heta D. vs Littler L.",
//        "status": "upcoming",
//        "duration": 9000,
//        "league_id": 4001,
//        "season_id": 42207,
//        "start_time": "2024-03-03T15:00:00+00:00",
//        "league_name": "UK Open",
//        "season_name": "UK Open 2024",
//        "away_team_id": 122828,
//        "home_team_id": 2136,
//        "status_reason": "Not started",
//        "tournament_id": 77173,
//        "away_team_name": "Littler L.",
//        "home_team_name": "Heta D.",
//        "tournament_name": "UK Open 2024",
//        "league_hash_image": "4954f966f6085b1f0f1466c57449b9d0010368f053869079beb3ce77a381780c",
//        "away_team_hash_image": "b000f6fbf013028e53b6b07d4d99f928c45a3da8699edcf07ca69ed8e939689a",
//        "home_team_hash_image": "25907b4ee61193eee05e2525bde492e3b0cdb03b456bb64e18f18c89925efbf9",
//        "tournament_importance": 0
//        },
//        {
//        "id": 40471,
//        "name": "Evans R. vs Cross R.",
//        "status": "upcoming",
//        "duration": 9000,
//        "league_id": 4001,
//        "season_id": 42207,
//        "start_time": "2024-03-03T14:00:00+00:00",
//        "league_name": "UK Open",
//        "season_name": "UK Open 2024",
//        "away_team_id": 1997,
//        "home_team_id": 47570,
//        "status_reason": "Not started",
//        "tournament_id": 77173,
//        "away_team_name": "Cross R.",
//        "home_team_name": "Evans R.",
//        "tournament_name": "UK Open 2024",
//        "league_hash_image": "4954f966f6085b1f0f1466c57449b9d0010368f053869079beb3ce77a381780c",
//        "away_team_hash_image": "0ee0e38aa04003eee9fcb0ca14eab6218403833a52051e6da44f17788fddc0d9",
//        "home_team_hash_image": "e576587475f7dd979c5056ba6a132becfb1816f2d46257dadafc61727f328b82",
//        "tournament_importance": 0
//        }
//        ]
//        }
//        ]


