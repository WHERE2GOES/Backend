package backend.greatjourney.domain.contents.dto;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "response")
public class ApiResponse {

    private Header header;
    private Body body;

    @XmlElement(name = "header")
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @XmlElement(name = "body")
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Header {
        private String resultCode;
        private String resultMsg;

        @XmlElement(name = "resultCode")
        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        @XmlElement(name = "resultMsg")
        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }

    @Data
    public static class Body {
        private List<Item> items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;

        @XmlElement(name = "items")
        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        @XmlElement(name = "numOfRows")
        public int getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(int numOfRows) {
            this.numOfRows = numOfRows;
        }

        @XmlElement(name = "pageNo")
        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        @XmlElement(name = "totalCount")
        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public static class Item {
            private String addr1;
            private String addr2;
            private String contentid;
            private String title;
            private String dist;
            // 필요한 다른 필드 추가...

            @XmlElement(name = "addr1")
            public String getAddr1() {
                return addr1;
            }

            public void setAddr1(String addr1) {
                this.addr1 = addr1;
            }

            @XmlElement(name = "addr2")
            public String getAddr2() {
                return addr2;
            }

            public void setAddr2(String addr2) {
                this.addr2 = addr2;
            }

            @XmlElement(name = "contentid")
            public String getContentid() {
                return contentid;
            }

            public void setContentid(String contentid) {
                this.contentid = contentid;
            }

            @XmlElement(name = "title")
            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            @XmlElement(name = "dist")
            public String getDist() {
                return dist;
            }

            public void setDist(String dist) {
                this.dist = dist;
            }

            // 다른 필드의 getter/setter 추가...
        }
    }
}
