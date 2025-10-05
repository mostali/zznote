function addMouseTracking() {

    let startX, startY, endX, endY, startTime;

    window.addEventListener('mousedown', function(event) {
        startX = event.clientX;
        startY = event.clientY;
        startTime = Date.now();
    });

    window.addEventListener('mouseup', function(event) {
        endX = event.clientX;
        endY = event.clientY;
        const endTime = Date.now();

        const duration = endTime - startTime;

        let direction = '';
        if (startY > endY) direction += 'top';
        else if (startY < endY) direction += 'bottom';

        if (startX > endX) direction += (direction ? '-' : '') + 'left';
        else if (startX < endX) direction += (direction ? '-' : '') + 'right';

        const coordinates = {
            x: startX,
            y: startY,
            x2: endX,
            y2: endY,
            duration: duration,
            direction: direction || 'nomove'
        };

        onBool(coordinates);
    });
}

addMouseTracking();
