package backend.greatjourney.global.gpt.dto;


import java.util.List;

public record GptTop3LiteResponse(
	List<Item> top3
) {
	public record Item(String name, String image_url, String country, String courseId) {}
}

