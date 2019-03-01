import java.math.BigInteger;

public class Player {
    private String steamid, name;
    private BigInteger score, kills, deaths, assists, shots, hits, headshots;

    public String getSteamid() {
        return steamid;
    }

    public void setSteamid(String steamid) {
        this.steamid = steamid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getScore() {
        return score;
    }

    public void setScore(BigInteger score) {
        this.score = score;
    }

    public BigInteger getKils() {
        return kills;
    }

    public void setKils(BigInteger kills) {
        this.kills = kills;
    }

    public BigInteger getDeaths() {
        return deaths;
    }

    public void setDeaths(BigInteger deaths) {
        this.deaths = deaths;
    }

    public BigInteger getAssists() {
        return assists;
    }

    public void setAssists(BigInteger assists) {
        this.assists = assists;
    }

    public BigInteger getShots() {
        return shots;
    }

    public void setShots(BigInteger shots) {
        this.shots = shots;
    }

    public BigInteger getHits() {
        return hits;
    }

    public void setHits(BigInteger hits) {
        this.hits = hits;
    }

    public BigInteger getHeadshots() {
        return headshots;
    }

    public void setHeadshots(BigInteger headshots) {
        this.headshots = headshots;
    }

    // JUST FOR TESTING
    @Override
    public String toString() {
        return "Player{" +
                "steamid='" + steamid + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", assists=" + assists +
                ", shots=" + shots +
                ", hits=" + hits +
                ", headshots=" + headshots +
                '}';
    }
}
