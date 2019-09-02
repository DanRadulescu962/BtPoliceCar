/* Radulescu Dan, 333CB */

#define F_CPU 16000000

#include <avr/io.h>
#include <util/delay.h>
#include "usart.h"
#include <avr/interrupt.h>

#define REF 15624
#define INCR 5
#define AUX 10

volatile int alreadyLed;
volatile int alreadyHorn;

/* At each half second switch leds and alarm note */
ISR(TIMER1_COMPA_vect)
{
	if (alreadyLed == 1) {
		PORTB ^= (1 << PB5);
		PORTB ^= (1 << PB6);
		PORTB ^= (1 << PB7);
	}

	if (alreadyHorn == 1) {
		TCCR2B ^= (1 << CS00);
	}
}

void init_engines()
{
	DDRC = 0;
	PORTC = 0;
	DDRC |= (1 << PC0) | (1 << PC1) | (1 << PC2) | (1 << PC3);

	// Set PWM - for engine speed - values between 150 and 255
	DDRB = 0;
	DDRB |= (1 << PB3) | (1 << PB4);

	TCCR0A = 0;
	TCCR0B = 0;

	TCCR0A |= (3 << WGM00);
	OCR0A = OCR0B = 190;

	TCCR0A |= (2 << COM0A0);
	TCCR0A |= (2 << COM0B0);

	TCCR0B |= (3 << CS00);
}

void init_LEDs()
{
	alreadyLed = 0;
	DDRB |= (1 << PB5) | (1 << PB6) | (1 << PB7);

	// Set LEDs timer
	TCCR1A = 0;
	TCCR1B = 0;

	TCCR1A |= (2 << COM1A0) | (2 << COM1B0);
	OCR1A = 7812;
	TCCR1B |= (1 << WGM12);

	TIMSK1 = 0;
	TIMSK1 |= (1 << OCIE1A);

	TCCR1B |= (5 << CS10);
}

void start_LEDs()
{
	PORTB &= ~(1 << PB5);
	PORTB &= ~(1 << PB7);
	PORTB |= (1 << PB6);
}

void stop_LEDs()
{
	PORTB &= ~(1 << PB5);
	PORTB &= ~(1 << PB6);
	PORTB &= ~(1 << PB7);
}

void init_Buzzer()
{
	alreadyHorn = 0;
	// Set Buzzer
	OCR1B = AUX * REF / 1000;
	TCCR2A |= (3 << WGM20);
	TCCR2A |= (2 << COM2A0);
	TCCR2A |= (2 << COM2B0);
	TCCR2B = 0;
}

void start_Buzzer()
{
	DDRD |= (1 << PD6);
	TCCR2B |= (5 << CS00);
}

void stop_Buzzer()
{
	DDRD &= ~(1 << PD6);
	TCCR2B = 0;
}

void moveForward()
{
	PORTC |= (1 << PC0);
	PORTC &= ~(1 << PC2);
	PORTC &= ~(1 << PC1);
	PORTC |= (1 << PC3);
}

void moveBackward()
{
	PORTC &= ~(1 << PC0);
	PORTC |= (1 << PC2);
	PORTC |= (1 << PC1);
	PORTC &= ~(1 << PC3);
}

void moveRight()
{
	PORTC |= (1 << PC0);
	PORTC &= ~(1 << PC2);
	PORTC &= ~(1 << PC1);
	PORTC &= ~(1 << PC3);
}

void moveLeft()
{
	PORTC &= ~(1 << PC0);
	PORTC &= ~(1 << PC2);
	PORTC &= ~(1 << PC1);
	PORTC |= (1 << PC3);
}

int main() {
	//_delay_ms(1000);
	init_engines();
	init_LEDs();
	init_Buzzer();

	USART0_init();
	sei();
	char c1 = '0', lastChar = '0';
	while (1) {

		c1 = USART0_receive();
		if (c1 == 'W') {
			lastChar = 'W';
			moveForward();
    		}
		else if (c1 == 'w' && lastChar == 'W') {
			PORTC = 0;
		}
		else if (c1 == 'S') {
			lastChar = 'S';
			moveBackward();
		}
		else if (c1 == 's' && lastChar == 'S') {
			PORTC = 0;
		}
		else if (c1 == 'L') {
			lastChar = 'L';
			moveLeft();
		}
		else if (c1 == 'l' && lastChar == 'L') {
			PORTC = 0;
		}
		else if (c1 == 'R') {
			lastChar = 'R';
			moveRight();
		}
		else if (c1 == 'r' && lastChar == 'R') {
			PORTC = 0;
		}
		else if (c1 == 'H') {
			if (alreadyHorn == 0) {
				start_Buzzer();
				alreadyHorn = 1;
			}
			else {
				alreadyHorn = 0;
				stop_Buzzer();
			}
		}
		else if (c1 == 'G') {
			if (alreadyLed == 0) {
				start_LEDs();
				alreadyLed = 1;
			}
			else {
				stop_LEDs();
				alreadyLed = 0;
			}
		}
		else if (c1 == 'z') {
			// Received new speed
			int nr = 0;
			c1 = USART0_receive();
			nr = nr * 10 + (c1 - '0');
			c1 = USART0_receive();
			nr = nr * 10 + (c1 - '0');
			c1 = USART0_receive();
			nr = nr * 10 + (c1 - '0');
			OCR0A = OCR0B = nr;
		}
	}

	return 0;
}
