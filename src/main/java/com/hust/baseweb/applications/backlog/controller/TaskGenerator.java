package com.hust.baseweb.applications.backlog.controller;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TaskGenerator {

    private final static String[] category = {"BUG", "OTHER", "REQUEST", "TASK"};
    private final static String[] status = {"TASK_OPEN", "TASK_INPROGRESS"};
    private final static String[] priority = {"0_HIGH", "1_NORMAL", "2_LOW"};
    private final static String[] description = {
        "Lorem Ipsum chỉ đơn giản là một đoạn văn bản giả, được dùng vào việc trình bày và dàn trang phục vụ cho in ấn",
        "Lorem Ipsum đã được sử dụng như một văn bản chuẩn cho ngành công nghiệp in ấn từ những năm 1500, khi một họa sĩ vô danh ghép nhiều đoạn văn bản với nhau để tạo thành một bản mẫu văn bản",
        "Đoạn văn bản này không những đã tồn tại năm thế kỉ, mà khi được áp dụng vào tin học văn phòng, nội dung của nó vẫn không hề bị thay đổi",
        "Nó đã được phổ biến trong những năm 1960 nhờ việc bán những bản giấy Letraset in những đoạn Lorem Ipsum, và gần đây hơn, được sử dụng trong các ứng dụng dàn trang, như Aldus PageMaker",
        "Trái với quan điểm chung của số đông, Lorem Ipsum không phải chỉ là một đoạn văn bản ngẫu nhiên",
        "Người ta tìm thấy nguồn gốc của nó từ những tác phẩm văn học la-tinh cổ điển xuất hiện từ năm 45 trước Công Nguyên, nghĩa là nó đã có khoảng hơn 2000 tuổi",
        "Một giáo sư của trường Hampden-Sydney College (bang Virginia - Mỹ) quan tâm tới một trong những từ la-tinh khó hiểu nhất, \"consectetur\", trích từ một đoạn của Lorem Ipsum, và đã nghiên cứu tất cả các ứng dụng của từ này trong văn học cổ điển, để từ đó tìm ra nguồn gốc không thể chối cãi của Lorem Ipsum",
        "Thật ra, nó được tìm thấy trong các đoạn 1.10.32 và 1.10.33 của \"De Finibus Bonorum et Malorum\" (Đỉnh tối thượng của Cái Tốt và Cái Xấu) viết bởi Cicero vào năm 45 trước Công Nguyên",
        "Cuốn sách này là một luận thuyết đạo lí rất phổ biến trong thời kì Phục Hưng. Dòng đầu tiên của Lorem Ipsum, \"Lorem ipsum dolor sit amet...\" được trích từ một câu trong đoạn thứ 1.10.32",
        "Chúng ta vẫn biết rằng, làm việc với một đoạn văn bản dễ đọc và rõ nghĩa dễ gây rối trí và cản trở việc tập trung vào yếu tố trình bày văn bản",
        "Lorem Ipsum có ưu điểm hơn so với đoạn văn bản chỉ gồm nội dung kiểu \"Nội dung, nội dung, nội dung\" là nó khiến văn bản giống thật hơn, bình thường hơn",
        "Nhiều phần mềm thiết kế giao diện web và dàn trang ngày nay đã sử dụng Lorem Ipsum làm đoạn văn bản giả, và nếu bạn thử tìm các đoạn \"Lorem ipsum\" trên mạng thì sẽ khám phá ra nhiều trang web hiện vẫn đang trong quá trình xây dựng",
        "Có nhiều phiên bản khác nhau đã xuất hiện, đôi khi do vô tình, nhiều khi do cố ý (xen thêm vào những câu hài hước hay thông tục)",
        "Có rất nhiều biến thể của Lorem Ipsum mà bạn có thể tìm thấy, nhưng đa số được biến đổi bằng cách thêm các yếu tố hài hước, các từ ngẫu nhiên có khi không có vẻ gì là có ý nghĩa",
        "Nếu bạn định sử dụng một đoạn Lorem Ipsum, bạn nên kiểm tra kĩ để chắn chắn là không có gì nhạy cảm được giấu ở giữa đoạn văn bản",
        "Tất cả các công cụ sản xuất văn bản mẫu Lorem Ipsum đều được làm theo cách lặp đi lặp lại các đoạn chữ cho tới đủ thì thôi, khiến cho lipsum.com trở thành công cụ sản xuất Lorem Ipsum đáng giá nhất trên mạng",
        "Trang web này sử dụng hơn 200 từ la-tinh, kết hợp thuần thục nhiều cấu trúc câu để tạo ra văn bản Lorem Ipsum trông có vẻ thật sự hợp lí",
        "Nhờ thế, văn bản Lorem Ipsum được tạo ra mà không cần một sự lặp lại nào, cũng không cần chèn thêm các từ ngữ hóm hỉnh hay thiếu trật tự",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
        "Proin nec elit non leo suscipit rhoncus non non urna",
        "Aenean ultricies nulla pretium, aliquet risus sed, finibus quam",
        "Curabitur ut tortor finibus, iaculis purus in, dapibus odio",
        "Aliquam eget ligula elementum, consectetur tortor a, commodo arcu",
        "Phasellus congue ligula et felis tempus faucibus eget id tellus",
        "Nulla eu neque eget urna vehicula elementum nec sed urna",
        "Cras vehicula odio ut varius consectetur",
        "Cras sed nunc sit amet velit maximus vehicula",
        "Suspendisse a enim a ante congue luctus",
        "Proin tincidunt orci vitae urna venenatis sagittis",
        "Praesent maximus ipsum tincidunt blandit euismod",
        "In dictum ipsum id massa laoreet, quis consequat leo imperdiet",
        "Donec tempor libero aliquet purus imperdiet molestie",
        "Integer tincidunt diam id augue commodo scelerisque",
        "Integer at dui quis quam suscipit pellentesque",
        "Aliquam ornare magna id tellus tempor, ac pulvinar ex elementum",
        "Fusce pharetra lectus a mi dictum porttitor",
        "Nullam at augue rhoncus, ullamcorper erat in, vestibulum ligula",
    };

    private final static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static Date start1 = new Date();
    private static Date end1 = new Date();
    private static Date end2 = new Date();

    static {
        try {
            start1 = formatter.parse("21/01/2021 12:00:00");
            end1 = formatter.parse("28/01/2021 18:00:00");
            end2 = formatter.parse("04/02/2021 18:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
            .current()
            .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

//    public static void main(String []args) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//
//        Date date1 = formatter.parse("20/01/2021 12:00:00");
//        Date date2 = formatter.parse("26/01/2021 18:00:00");
//
//        System.out.println(start1);
//    }


    public static CreateBacklogTaskInputModel taskGenerator(int num, UUID projectId) {
        Random rand = new Random();

        String backlogTaskName = "Task " + "0000".substring(String.valueOf(num).length()) + num + "_" + rand.nextInt(999);
        String categoryId = category[rand.nextInt(category.length) % category.length];
        String backlogDescription = description[rand.nextInt(description.length) % description.length];
        String statusId = status[rand.nextInt(status.length) % status.length];
        String priorityId = priority[rand.nextInt(priority.length) % priority.length];

        CreateBacklogTaskInputModel task = new CreateBacklogTaskInputModel();
        task.setBacklogTaskName(backlogTaskName);
        task.setCategoryId(categoryId);
        task.setBacklogDescription(backlogDescription);
        task.setStatusId(statusId);
        task.setPriorityId(priorityId);
        task.setBacklogProjectId(projectId);
        task.setFromDate(between(start1, end1));
        task.setDueDate(between(task.getFromDate(), end2));
        task.setAttachmentPaths(new String[]{});

        return task;
    }
}
