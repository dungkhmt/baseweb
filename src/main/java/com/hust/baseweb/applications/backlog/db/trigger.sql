create trigger backlog_project_last_updated_stamp
    before update
    on backlog_project
    for each row
execute procedure set_last_updated_stamp();

create trigger backlog_task_last_updated_stamp
    before update
    on backlog_task
    for each row
execute procedure set_last_updated_stamp();

create trigger backlog_task_assignable_last_updated_stamp
    before update
    on backlog_task_assignable
    for each row
execute procedure set_last_updated_stamp();

create trigger backlog_task_assignment_last_updated_stamp
    before update
    on backlog_task_assignment
    for each row
execute procedure set_last_updated_stamp();

create trigger backlog_task_category_last_updated_stamp
    before update
    on backlog_task_category
    for each row
execute procedure set_last_updated_stamp();
