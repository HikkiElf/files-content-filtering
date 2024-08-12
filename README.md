# Инструкция
> Java version: 22.0.2

Данная консольная программа позволяет сортировать данные из txt файлов на строки, целые и вещественные числа, записывая их в отдельные txt файлы.

### Для запуска программы необходимо:
- Скачать [JDK 22](https://www.oracle.com/cis/java/technologies/downloads/)
- Скачать util.jar из репозитория

### Работа с программой:
`java -jar util.jar [-s|-f|-a|-o <dir>|-p <prefix>] 1.txt ... n.txt`

результат: файлы strings.txt, integers.txt, floats.txt

#### Аргументы:
-s: показывает краткую статистику о сохранённых файлах

`strings.txt n строк сохранено
 integers.txt n строк сохранено
 floats.txt n строк сохранено`
