package nl.slindenau.openmusicscrobbler.discogs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author slindenau
 * https://github.com/s-lindenau
 * Licence: GPLv3
 */
public class Pagination extends DiscogsApiResponse {

    public int page;
    public int pages;
    @JsonProperty("per_page")
    public int itemsPerPage;
    public int items;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}
