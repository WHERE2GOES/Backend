package backend.greatjourney.domain.course.config;


import backend.greatjourney.domain.course.domain.Course;
import backend.greatjourney.domain.course.domain.CoursePoint;
import backend.greatjourney.domain.course.domain.Place;
import backend.greatjourney.domain.course.repository.CoursePointRepository;
import backend.greatjourney.domain.course.repository.CourseRepository;
import backend.greatjourney.domain.course.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Profile("init")
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepo;
    private final CoursePointRepository pointRepo;
    private final PlaceRepository placeRepo;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 한 번이라도 로드된 적 있으면 건너뛴다
        if (courseRepo.count() > 0) return;

        /* --------------------------------------------------
         * 1) 코스(노선) 정보  – Excel
         * -------------------------------------------------- */
        try (InputStream is = new ClassPathResource("data/course_codebook.xlsx").getInputStream()) {
            Workbook wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // header
                Cell idCell = row.getCell(0);
                int id;
                if (idCell.getCellType() == CellType.STRING) {
                    id = Integer.parseInt(idCell.getStringCellValue().trim());
                } else {
                    id = (int) idCell.getNumericCellValue();
                }
                String name = row.getCell(1).getStringCellValue();
                courseRepo.save(new Course(id, name));
            }
        }

        /* --------------------------------------------------
         * 2) 노선 좌표 – CSV
         * -------------------------------------------------- */
        try (InputStreamReader reader = new InputStreamReader(
                new ClassPathResource("data/route_points.csv").getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord rec : parser) {
                String seqStr = rec.get("순서").trim();
                // 숫자가 아니면 skip
                if (!seqStr.matches("\\d+")) continue;

                int seq      = Integer.parseInt(seqStr);
                int courseId = Integer.parseInt(rec.get("국토종주 자전거길").trim());
                double lat   = Double.parseDouble(rec.get("위도(LINE_XP)").trim());
                double lon   = Double.parseDouble(rec.get("경도(LINE_YP)").trim());

                pointRepo.save(new CoursePoint(null,
                        courseRepo.getReferenceById(courseId), seq, lat, lon));
            }
        }

        /* --------------------------------------------------
         * 3) 주변 시설 – CSV
         * -------------------------------------------------- */
        try (InputStreamReader reader = new InputStreamReader(
                new ClassPathResource("data/places.csv").getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord rec : parser) {
                String category = rec.get("구분");
                String name     = rec.get("이름");
                double lon      = Double.parseDouble(rec.get("경도"));
                double lat      = Double.parseDouble(rec.get("위도"));

                placeRepo.save(new Place(null, name, category, lat, lon));
            }
        }
    }
}