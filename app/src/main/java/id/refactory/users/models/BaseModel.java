package id.refactory.users.models;

import java.util.List;

public class BaseModel {
    private List<ResultsItem> results;
    private Info info;

    public void setResults(List<ResultsItem> results) {
        this.results = results;
    }

    public List<ResultsItem> getResults() {
        return results;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Info getInfo() {
        return info;
    }
}