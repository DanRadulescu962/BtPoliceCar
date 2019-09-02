BtServer.hex: BtServer.elf
	avr-objcopy  -j .text -j .data -O ihex BtServer.elf BtServer.hex

BtServer.elf: usart.o BtPoliceCarServer.o
	avr-g++ -mmcu=atmega324p -Os -Wall -o BtServer.elf $^

usart.o: usart.c
	avr-g++ -mmcu=atmega324p -Os -Wall -c usart.c

BtPoliceCarServer.o: BtPoliceCarServer.c
	avr-g++ -mmcu=atmega324p -Os -Wall -c BtPoliceCarServer.c

clean:
	rm -rf *.o *.elf *.hex
