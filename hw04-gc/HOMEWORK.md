## Результаты запуска приложения с различным размером хипа
### Тесты производились на Macbook air M1 ARM64 16GB, Liberica Standard JDK 17

|Heap size|Result|Throughput|Latency avg pause|Latency max pause|Total pause time|Total Concurrent time|
|:---:|:---:|:---:|:---:|:---:| :---:|:---|
|256mb|OutOfMemoryError| 
|336 mb|spend msec:7999, sec:7|52.895%|5.70 ms|24.2 ms|3 sec 837 ms|6 sec 796 ms|
|512 mb|spend msec:5501, sec:5|61.045%|9.83 ms|32.1 ms|2 sec 153 ms|2 sec 970 ms|
|768 mb|spend msec:4395, sec:4|66.906%|19.4 ms|42.6 ms|	1 sec 452 ms|531 ms|
|1024 mb|spend msec:4431, sec:4|67.77%|21.7 ms|57.2 ms|1 sec 435 ms|682 ms|
|1280 mb|spend msec:4092, sec:4|69.093%|30.4 ms|71.5 ms|1 sec 246 ms|73.9 ms|
|1536 mb|spend msec:3922, sec:3|74.034%|30.7 ms|84.0 ms|1 sec 13 ms|31.7 ms|
|`1792 mb`|`spend msec:3405, sec:3`|`85.512%`|`18.8 ms`|`43.5 ms`|`469 ms`|`0`|
|2048 mb|spend msec:3781, sec:3|77.03%|35.4 ms|55.2 ms|	849 ms|0|
|2304 mb|spend msec:4042, sec:4|74.111%|44.5 ms|93.3 ms|1 sec 23 ms|0|

>***Вывод:*** Лучший результат работы не оптимизированного приложения достигается при размере хипа 1792 mb. 
>Со значения 768 mb, скорость работы меняется не значительно, но существенно увеличивается throughput и сокращается 
>суммарная длительность stop the world пауз.

### После оптимизаници

|Heap size|Result|Throughput|Latency avg pause|Latency max pause|Total pause time|Total Concurrent time|
|:---:|:---:|:---:|:---:|:---:| :---:|:---|
|256mb|spend msec:1786, sec:1|51.555%|7.06 ms|30.8 ms|868 ms|1 sec 87 ms|
|512 mb|spend msec:1183, sec:1|63.267%|14.1 ms|31.1 ms|438 ms|292 ms|
|768 mb|spend msec:1073, sec:1|70.615%|19.4 ms|37.0 ms|	310 ms|48.6 ms|
|1024 mb|spend msec:1084, sec:1|67.984%|19.6 ms|44.4 ms|314 ms|59.1 ms|
|1280 mb|spend msec:1134, sec:1|68.124%|22.3 ms|50.8 ms|356 ms|98.4 ms|
|1536 mb|spend msec:1024, sec:1|74.132%|22.2 ms|67.9 ms|245 ms|0|
|1792 mb|spend msec:1155, sec:1|67.39%|31.3 ms|74.3 ms|377 ms|0|

>***Вывод:*** Размер хипа не влияет на работу приложения, однако, можно отметить, что 256mb не совсем достаточно для оптимальной работы. 