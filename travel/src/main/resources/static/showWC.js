
var _0x12f7 = ["\x72\x61\x6E\x64\x6F\x6D", "\x66\x6C\x6F\x6F\x72", "\x23\x32\x33\x33\x44\x34\x44", "\x23\x46\x45\x37\x46\x32\x44", "\x23\x46\x43\x43\x41\x34\x36", "\x23\x41\x31\x43\x31\x38\x31", "\x23\x35\x37\x39\x43\x38\x37", "\x23\x35\x38\x42\x37\x42\x38", "\x23\x46\x38\x43\x45\x33\x44", "\x23\x44\x46\x36\x31\x32\x37", "\x23\x46\x32\x45\x34\x42\x31", "\x23\x31\x32\x34\x30\x33\x45", "\x23\x43\x46\x37\x36\x36\x30", "\x23\x41\x44\x45\x30\x43\x34", "\x23\x31\x37\x37\x45\x38\x39", "\x23\x45\x39\x43\x34\x36\x41", "\x23\x46\x34\x41\x32\x36\x31", "\x23\x42\x46\x32\x31\x32\x45", "\x23\x46\x32\x35\x32\x37\x30", "\x23\x30\x34\x43\x34\x44\x39", "\x23\x39\x45\x42\x46\x32\x34", "\x23\x46\x32\x38\x42\x33\x30", "\x76\x61\x6C", "\x23\x63\x6F\x6C\x6F\x72\x53\x74", "\x23\x66\x6F\x6E\x74\x53\x74", "\x23\x72\x73\x53\x74", "\x23\x62\x67\x53\x74", "\x6C\x65\x6E\x67\x74\x68", "\x70\x6F\x77", "\x77\x66\x20\x69\x73\x20", "\x3B\x77\x66\x63\x20\x69\x73\x20", "\x6C\x6F\x67", "\x6D\x79\x5F\x63\x61\x6E\x76\x61\x73", "\x67\x65\x74\x45\x6C\x65\x6D\x65\x6E\x74\x42\x79\x49\x64", "\x62\x6F\x6C\x64"]; function chooseColor(_0x1236x2) { switch (_0x1236x2) { case 1: return ([_0x12f7[2], _0x12f7[3], _0x12f7[4], _0x12f7[5], _0x12f7[6]][Math[_0x12f7[1]](Math[_0x12f7[0]]() * 5)]); break; case 2: return ([_0x12f7[7], _0x12f7[8], _0x12f7[9], _0x12f7[10], _0x12f7[11]][Math[_0x12f7[1]](Math[_0x12f7[0]]() * 5)]); break; case 3: return ([_0x12f7[12], _0x12f7[13], _0x12f7[14], _0x12f7[15], _0x12f7[16]][Math[_0x12f7[1]](Math[_0x12f7[0]]() * 5)]); break; case 4: return ([_0x12f7[17], _0x12f7[18], _0x12f7[19], _0x12f7[20], _0x12f7[21]][Math[_0x12f7[1]](Math[_0x12f7[0]]() * 5)]); break } } drawCloud();
function drawCloud() {
    var _0x1236x2 = parseInt($(_0x12f7[23])[_0x12f7[22]]());
    var _0x1236x4 = $(_0x12f7[24])[_0x12f7[22]]();
    var _0x1236x5 = $(_0x12f7[25])[_0x12f7[22]]();
    var _0x1236x6 = $(_0x12f7[26])[_0x12f7[22]]();
    var _0x1236x7 = 0.5;
    var _0x1236x8 = lists[0][1] - lists[lists[_0x12f7[27]] - 1][1];
    var _0x1236x9; _0x1236x9 = 130.677390813746 * Math[_0x12f7[28]](_0x1236x8, -0.8730876542464); console[_0x12f7[31]](_0x12f7[29] + _0x1236x9 + _0x12f7[30] + _0x1236x8);
    if (_0x1236x5 == 0) { _0x1236x7 = 0 };
        WordCloud(document[_0x12f7[33]](_0x12f7[32]),
        { list: lists, gridSize: 8, weightFactor: _0x1236x9, fontFamily: _0x1236x4, fontWeight: _0x12f7[34],
            color: function () {
                return (chooseColor(_0x1236x2))
            },
            backgroundColor: _0x1236x6, rotationSteps: _0x1236x5, rotateRatio: _0x1236x7
        })
}
function reDraw() { drawCloud() }