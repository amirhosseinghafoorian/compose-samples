/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.owl.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.owl.ui.courses.CourseTabs
import com.example.owl.ui.theme.BlueTheme
import com.example.owl.ui.utils.ProvideImageLoader
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun OwlApp(finishActivity: () -> Unit) {
    ProvideWindowInsets {
        ProvideImageLoader {
            BlueTheme {
                val tabs = CourseTabs.values()
                val navController = rememberNavController()
                OwlScaffold(
                    bottomBar = { OwlBottomBar(navController = navController, tabs) }
                ) { innerPaddingModifier ->
                    NavGraph(
                        finishActivity = finishActivity,
                        navController = navController,
                        modifier = innerPaddingModifier
                    )
                }
            }
        }
    }
}

@Composable
fun OwlBottomBar(navController: NavController, tabs: Array<CourseTabs>) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
        ?: CourseTabs.FEATURED.route

    if (currentRoute in CourseTabs.values().map { it.route }) {
        BottomNavigation(
            Modifier.navigationBarsHeight(additional = 56.dp)
        ) {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                    label = { Text(stringResource(tab.title).toUpperCase()) },
                    selected = currentRoute == tab.route,
                    onClick = {
                        if (tab.route != currentRoute) {
                            navController.navigate(tab.route) {
                                popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        }
                    },
                    alwaysShowLabel = false,
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = LocalContentColor.current,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
fun OwlScaffold(
    bottomBar: @Composable () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.primarySurface,
        bottomBar = bottomBar
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        content(modifier)
    }
}
