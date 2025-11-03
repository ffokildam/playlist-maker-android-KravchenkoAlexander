package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PlaylistMakerApp() }
    }
}

@Composable
@Preview
fun PlaylistMakerApp() {
    // общий белый фон
    Surface(color = Color.White) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // синяя шапка
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3772E7))
                    .height(80.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Playlist maker",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(start = 16.dp, bottom = 20.dp)
                )
            }

            // белый блок с пунктами меню
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                color = Color.White, // один цвет с фоном
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-16).dp)
            ) {
                val context = androidx.compose.ui.platform.LocalContext.current
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White) // дублируем, чтобы MenuItem совпадал
                ) {
                    MenuItem("Поиск", R.drawable.ic_search) {
                        context.startActivity(Intent(context, SearchActivity::class.java))
                    }
                    MenuItem("Плейлисты", R.drawable.ic_playlist) { /* TODO */ }
                    MenuItem("Избранное", R.drawable.ic_favorite) { /* TODO */ }
                    MenuItem("Настройки", R.drawable.ic_settings) {
                        context.startActivity(Intent(context, SettingsActivity::class.java))
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(title: String, iconRes: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White) // теперь совпадает с общим фоном
            .clickable(onClick = onClick)
            .padding(horizontal = 30.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "arrow",
            tint = Color(0xFFB0B0B0)
        )
    }
}
