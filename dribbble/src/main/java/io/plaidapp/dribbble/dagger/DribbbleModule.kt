/*
 * Copyright 2018 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.plaidapp.dribbble.dagger

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import dagger.Module
import dagger.Provides
import io.plaidapp.core.dagger.dribbble.DribbbleDataModule
import io.plaidapp.core.data.CoroutinesContextProvider
import io.plaidapp.core.dribbble.data.ShotsRepository
import io.plaidapp.core.ui.widget.ElasticDragDismissFrameLayout
import io.plaidapp.dribbble.domain.GetShareShotInfoUseCase
import io.plaidapp.dribbble.ui.shot.ShotActivity
import io.plaidapp.dribbble.ui.shot.ShotViewModel
import io.plaidapp.dribbble.ui.shot.ShotViewModelFactory

/**
 * Module providing injections for the :dribbble feature module.
 */
@Module(includes = [DribbbleDataModule::class])
class DribbbleModule(private val activity: ShotActivity, private val shotId: Long) {

    @Provides
    fun provideContext(): Context = activity

    @Provides
    fun shotViewModel(factory: ShotViewModelFactory): ShotViewModel {
        return ViewModelProviders.of(activity, factory).get(ShotViewModel::class.java)
    }

    @Provides
    fun chromeFader(): ElasticDragDismissFrameLayout.SystemChromeFader {
        return object : ElasticDragDismissFrameLayout.SystemChromeFader(activity) {
            override fun onDragDismissed() {
                activity.setResultAndFinish()
            }
        }
    }

    @Provides
    fun shotViewModelFactory(
        shotsRepository: ShotsRepository,
        shareShotInfoUseCase: GetShareShotInfoUseCase,
        coroutinesContextProvider: CoroutinesContextProvider
    ): ShotViewModelFactory {
        return ShotViewModelFactory(
            shotId,
            shotsRepository,
            shareShotInfoUseCase,
            coroutinesContextProvider
        )
    }
}
